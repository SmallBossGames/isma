package ru.nstu.isma.intg.server.core;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.kryonet.rmi.ObjectSpace;
import com.google.common.base.Preconditions;
import com.google.common.base.Throwables;
import com.google.common.collect.Sets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.nstu.isma.intg.api.calcmodel.DaeSystem;
import ru.nstu.isma.intg.api.calcmodel.DaeSystemChangeSet;
import ru.nstu.isma.intg.api.methods.IntegrationMethodRungeKutta;
import ru.nstu.isma.intg.api.methods.IntgPoint;
import ru.nstu.isma.intg.server.ClassDataProvider;
import ru.nstu.isma.intg.server.ComputeEngine;
import ru.nstu.isma.intg.server.RemoteObjects;
import ru.nstu.isma.intg.server.models.*;
import ru.nstu.isma.intg.server.security.SecurityManagerWithAllPermissions;
import ru.nstu.isma.intg.server.services.ServiceFacade;
import ru.nstu.isma.intg.server.utils.BinaryObjectDeserializer;

import java.io.IOException;
import java.io.Serializable;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ComputeEngineServer implements ComputeEngine, Serializable {

    private static final int CALCULATION_TIMEOUT = 5;
    private static final TimeUnit CALCULATION_TIMEOUT_UNIT = TimeUnit.MINUTES;

    private static final int WRITE_BUFFER_SIZE = 20 * 1024 * 1024; //bytes
    private static final int OBJECT_BUFFER_SIZE = 20 * 1024 * 1024; //bytes

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final ServiceFacade serviceFacade;
    private Server server;
    private final Map<Integer, ClientContext> clientContexts = new HashMap<>();

    public ComputeEngineServer(ServiceFacade serviceFacade) {
        this.serviceFacade = serviceFacade;
    }

    @Override
    public void disconnect(int clientId) {
        ClientContext context = clientContexts.remove(clientId);
        if (context != null) {
            serviceFacade.getMpiService().sendMessageToEveryone(new Message(MessageType.CLEAR_RESOURCES));
            try {
                context.close();
            } catch (Exception e) {
                logger.error("Error on a client disconnect", e);
            }
        }
    }

    @Override
    public void loadIntgMethod(int clientId, LoadIntgMethodRequest request) {
        logger.debug("Got a request to load IntgMethod from client #{}.", clientId);
        ClientRequest<IntegrationMethodRungeKutta> clientRequest = buildClientRequest(clientId, request, IntegrationMethodRungeKutta.class);
        serviceFacade.getMpiService().sendMessageToEveryone(new Message(MessageType.LOAD_INTG_METHOD, clientRequest));
    }

    @Override
    public void loadDaeSystem(int clientId, LoadDaeSystemRequest request) {
        logger.debug("Got a request to load OdeSystem from client #{}.", clientId);
        ClientRequest<DaeSystem> clientRequest = buildClientRequest(clientId, request, DaeSystem.class);
        serviceFacade.getMpiService().sendMessageToEveryone(new Message(MessageType.LOAD_DAE_SYSTEM, clientRequest));
    }

    @Override
    public StepResponse step(int clientId, StepRequest intgPointRequest) {
        long startTimestamp = 0;
        if (logger.isDebugEnabled()) {
            startTimestamp = new Date().getTime();
        }

        try {
            return stepInternal(clientId, intgPointRequest);
        } catch (RuntimeException e) {
            String message = String.format("Error message: %s\nStack trace: %s",
                    e.getMessage(), Throwables.getStackTraceAsString(e));
            return StepResponse.createError(message);
        } finally {
            if (logger.isDebugEnabled()) {
                long duration = new Date().getTime() - startTimestamp;
                logger.debug("\n--- Request was processed in {} milliseconds", duration);
            }
        }
    }

    @Override
    public CalculateRhsResponse calculateRhs(int clientId, CalculateRhsRequest request) {
        try {
            return calculateRhsInternal(clientId, request);
        } catch (RuntimeException e) {
            String message = String.format("Error message: %s\nStack trace: %s",
                    e.getMessage(), Throwables.getStackTraceAsString(e));
            return CalculateRhsResponse.createError(message);
        }
    }

    @Override
    public void apply(int clientId, ApplyRequest request) {
        logger.debug("Got a request to apply DaeSystemChangeSet from client #{}.", clientId);
        ClientRequest<DaeSystemChangeSet> clientRequest = buildClientRequest(clientId, request,
                DaeSystemChangeSet.class);
        serviceFacade.getMpiService().sendMessageToEveryone(new Message(MessageType.APPLY, clientRequest));
    }

    public void start(int port) throws IOException {
        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManagerWithAllPermissions());
        }

        // com.esotericsoftware.minlog.Log.TRACE = true;

        server = new Server(WRITE_BUFFER_SIZE, OBJECT_BUFFER_SIZE);

        Kryo kryo = server.getKryo();
        ObjectSpace.registerClasses(kryo);
        RemoteObjects.getTransferableClasses().stream().forEach(kryo::register);

        server.start();
        server.bind(port);

        final ObjectSpace serverObjectSpace = new ObjectSpace();
        serverObjectSpace.setExecutor(Executors.newCachedThreadPool());
        serverObjectSpace.register(RemoteObjects.COMPUTE_ENGINE_SERVER_ID, this);

        server.addListener(new Listener() {
            @Override
            public void connected(final Connection connection) {
                super.connected(connection);
                ClassDataProvider clientClassDataProvider = ObjectSpace.getRemoteObject(
                        connection, RemoteObjects.CLIENT_CLASS_PROVIDER_ID, ClassDataProvider.class);
                clientContexts.put(connection.getID(), new ClientContext(connection, clientClassDataProvider));
                serverObjectSpace.addConnection(connection);
            }

            @Override
            public void disconnected(Connection connection) {
                disconnect(connection.getID());
                serverObjectSpace.removeConnection(connection);
                super.disconnected(connection);
            }
        });
    }

    public void stop() {
        if (server != null) {
            server.stop();
            server = null;
        }
    }

    private StepResponse stepInternal(int clientId, StepRequest request) {
        logger.debug("Got a request to make an integration step from client #{}.", clientId);

        final CountDownLatch latch = new CountDownLatch(1);
        final StepResponse[] resultArray = new StepResponse[1];

        serviceFacade.getMessageService().setMessageListener(MessageType.STEP_RESPONSE, message -> {
            StepResponse response = (StepResponse) message.getPayload();
            if (logger.isDebugEnabled()) {
                logger.debug("Got a calculation result (size: {})", response.getBinaryObject().getBytes().length);
            }
            try {
                resultArray[0] = response;
                latch.countDown();
            } catch (Exception ex) {
                logger.error("Failed to release a result latch", ex);
            }
        });

        ClientRequest<IntgPoint> clientRequest = buildClientRequest(clientId, request, IntgPoint.class);
        serviceFacade.getMpiService().sendMessageToEveryone(new Message(MessageType.STEP, clientRequest));

        logger.debug("Waiting for integration result...");
        try {
            latch.await(CALCULATION_TIMEOUT, CALCULATION_TIMEOUT_UNIT);
        } catch (InterruptedException e) {
            logger.error("Calculations were interrupted", e);
            throw new RuntimeException("Calculations were interrupted", e);
        } catch (Exception ex) {
            logger.error("Unexpected error during a wait for releasing a result latch", ex);
        }

        if (logger.isDebugEnabled()) {
            logger.debug("Sending a calculation result to a client (size: {})",
                    resultArray[0].getBinaryObject().getBytes().length);
        }

        return resultArray[0];
    }

    private CalculateRhsResponse calculateRhsInternal(int clientId, CalculateRhsRequest request) {
        logger.debug("Got a request to calculate rhs from client #{}.", clientId);

        final CountDownLatch latch = new CountDownLatch(1);
        final CalculateRhsResponse[] resultArray = new CalculateRhsResponse[1];

        serviceFacade.getMessageService().setMessageListener(MessageType.CALCULATE_RHS_RESPONSE, message -> {
            CalculateRhsResponse response = (CalculateRhsResponse) message.getPayload();

            if (logger.isDebugEnabled()) {
                logger.debug("Got a calculation rhs result (size: {})", response.getBinaryObject().getBytes().length);
            }
            try {
                resultArray[0] = response;
                latch.countDown();
            } catch (Exception ex) {
                logger.error("Failed to release a result latch", ex);
            }
        });

        ClientRequest<double[]> clientRequest = buildClientRequest(clientId, request, double[].class);
        serviceFacade.getMpiService().sendMessageToEveryone(new Message(MessageType.CALCULATE_RHS, clientRequest));

        logger.debug("Waiting for calculation rhs result...");
        try {
            latch.await(CALCULATION_TIMEOUT, CALCULATION_TIMEOUT_UNIT);
        } catch (InterruptedException e) {
            logger.error("Calculations were interrupted", e);
            throw new RuntimeException("Calculations were interrupted", e);
        } catch (Exception ex) {
            logger.error("Unexpected error during a wait for releasing a result latch", ex);
        }

        if (logger.isDebugEnabled()) {
            logger.debug("Sending a calculation rhs result to a client (size: {})",
                    resultArray[0].getBinaryObject().getBytes().length);
        }

        return resultArray[0];
    }

    private <T> ClientRequest<T> buildClientRequest(int clientId, Request<T> request, Class<T> objectClass) {
        Preconditions.checkNotNull(request, "request cannot be null");

        ClientContext context = clientContexts.get(clientId);
        if (context == null) {
            throw new RuntimeException("Client context is not found");
        }

        Set<String> loadedClassNames = new HashSet<>(context.getClassDataProvider().getLoadedClassNames());
        try {
            BinaryObjectDeserializer.deserialize(objectClass, request.getBinaryObject().getBytes(),
                    context.getRemoteClassLoader());
        } catch (IOException | ClassNotFoundException e) {
            logger.error("Failed to deserialize " + objectClass.getSimpleName(), e);
            throw new RuntimeException("Failed to deserialize " + objectClass.getSimpleName(), e);
        }

        Set<String> newLoadedClassNames =
                Sets.difference(context.getClassDataProvider().getLoadedClassNames(), loadedClassNames);
        Map<String, byte[]> loadedClassData = context.getLoadedClasses(newLoadedClassNames);

        return new ClientRequest<>(request, loadedClassData);
    }

}