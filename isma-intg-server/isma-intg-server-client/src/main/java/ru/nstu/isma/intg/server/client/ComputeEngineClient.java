package ru.nstu.isma.intg.server.client;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.rmi.ObjectSpace;
import com.esotericsoftware.kryonet.rmi.RemoteObject;
import com.google.common.base.Preconditions;
import ru.nstu.isma.intg.api.calcmodel.DaeSystem;
import ru.nstu.isma.intg.api.calcmodel.DaeSystemChangeSet;
import ru.nstu.isma.intg.api.methods.IntegrationMethodRungeKutta;
import ru.nstu.isma.intg.api.methods.IntgMethod;
import ru.nstu.isma.intg.api.methods.IntgPoint;
import ru.nstu.isma.intg.server.ComputeEngine;
import ru.nstu.isma.intg.server.RemoteObjects;
import ru.nstu.isma.intg.server.client.internal.ClientClassDataProvider;
import ru.nstu.isma.intg.server.models.*;
import ru.nstu.isma.intg.server.security.SecurityManagerWithAllPermissions;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.concurrent.Executors;
import java.util.function.Supplier;

public class ComputeEngineClient {

    private static final int CONNECT_TIMEOUT = 60 * 1000; //milliseconds
    private static final int RESPONSE_TIMEOUT = 6 * 60 * 1000; //6 minutes in milliseconds
    private static final int WRITE_BUFFER_SIZE = 20 * 1024 * 1024; //20 Mb in bytes
    private static final int OBJECT_BUFFER_SIZE = 20 * 1024 * 1024; //20 Mb in bytes

    private final ClientClassDataProvider clientClassProvider;

    private Client client;
    private ComputeEngine computeEngine;

    public ComputeEngineClient() {
        this(ComputeEngineClient.class.getClassLoader());
    }

    /**
     * @param classLoader cannot ne <tt>null</tt>.
     */
    public ComputeEngineClient(ClassLoader classLoader) {
        if (classLoader == null) {
            throw new IllegalArgumentException("classLoader is null");
        }
        this.clientClassProvider = new ClientClassDataProvider(classLoader);
    }

    public void connect(String serverHost, int serverPort) throws ComputeEngineClientException {
        if (serverHost == null) {
            throw new IllegalArgumentException("serverHost must be not null");
        }
        if (serverPort <= 0) {
            throw new IllegalArgumentException("serverPort must be positive number");
        }

        if (System.getSecurityManager() == null) {
            System.setSecurityManager(new SecurityManagerWithAllPermissions());
        }

        try {
            // com.esotericsoftware.minlog.Log.TRACE = true;

            client = new Client(WRITE_BUFFER_SIZE, OBJECT_BUFFER_SIZE);

            Kryo kryo = client.getKryo();
            ObjectSpace.registerClasses(kryo);
            RemoteObjects.getTransferableClasses().stream().forEach(kryo::register);

            client.start();

            final ObjectSpace clientObjectSpace = new ObjectSpace();
            clientObjectSpace.setExecutor(Executors.newCachedThreadPool());
            clientObjectSpace.register(RemoteObjects.CLIENT_CLASS_PROVIDER_ID, clientClassProvider);

            client.addListener(new Listener() {
                @Override
                public void connected(final Connection connection) {
                    clientObjectSpace.addConnection(connection);
                }
            });

            client.connect(CONNECT_TIMEOUT, serverHost, serverPort);

            computeEngine = ObjectSpace.getRemoteObject(
                    client, RemoteObjects.COMPUTE_ENGINE_SERVER_ID, ComputeEngine.class);

            ((RemoteObject) computeEngine).setResponseTimeout(RESPONSE_TIMEOUT);

        } catch (IOException e) {
            throw new ComputeEngineClientException("Failed to connect to Integration Server", e);
        }
    }

    public void disconnect() {
        if (client != null) {
            try {
                invokeComputeEngine(() -> computeEngine.disconnect(client.getID()));
            } catch (ComputeEngineClientException e) {
                // Do nothing
            }
            client.stop();
            client = null;
            computeEngine = null;
        }
    }

    public void loadIntgMethod(IntegrationMethodRungeKutta intgMethod) throws ComputeEngineClientException {
        Preconditions.checkNotNull(intgMethod, "intgMethod cannot be null");
        LoadIntgMethodRequest request = new LoadIntgMethodRequest(serialize(intgMethod));
        invokeComputeEngine(() -> computeEngine.loadIntgMethod(client.getID(), request));
    }

    public void loadDaeSystem(DaeSystem daeSystem) throws ComputeEngineClientException {
        Preconditions.checkNotNull(daeSystem, "daeSystem cannot be null");
        LoadDaeSystemRequest request = new LoadDaeSystemRequest(serialize(daeSystem));
        invokeComputeEngine(() -> computeEngine.loadDaeSystem(client.getID(), request));
    }

    public double[][] calculateRhs(double[] yForDe) throws ComputeEngineClientException {
        Preconditions.checkNotNull(yForDe, "yForDe cannot be null");
        CalculateRhsRequest request = new CalculateRhsRequest(serialize(yForDe));
        CalculateRhsResponse response = invokeComputeEngine(() -> computeEngine.calculateRhs(client.getID(), request));
        return readResponse(response);
    }

    public void apply(DaeSystemChangeSet changeSet) throws ComputeEngineClientException {
        Preconditions.checkNotNull(changeSet, "changeSet cannot be null");
        ApplyRequest request = new ApplyRequest(serialize(changeSet));
        invokeComputeEngine(() -> computeEngine.apply(client.getID(), request));
    }

    public IntgPoint step(IntgPoint intgPoint) throws ComputeEngineClientException {
        Preconditions.checkNotNull(intgPoint, "intgPoint cannot be null");
        StepRequest request = new StepRequest(serialize(intgPoint));
        StepResponse response = invokeComputeEngine(() -> computeEngine.step(client.getID(), request));
        return readResponse(response);
    }

    private <T> T invokeComputeEngine(Supplier<T> supplier) throws ComputeEngineClientException {
        checkConnectionToServer();
        try {
            return supplier.get();
        } catch (RuntimeException e) {
            throw new ComputeEngineClientException("Failed to invoke remote compute server", e);
        }
    }

    private void invokeComputeEngine(Runnable runnable) throws ComputeEngineClientException {
        invokeComputeEngine(() -> {
            runnable.run();
            return null;
        });
    }

    private void checkConnectionToServer() throws ComputeEngineClientException {
        if (computeEngine == null) {
            throw new ComputeEngineClientException("Client is not connected");
        }
    }

    private <T> BinaryObject<T> serialize(T object) throws ComputeEngineClientException {
        try {
            return BinaryObject.serialize(object);
        } catch (IOException e) {
            throw new ComputeEngineClientException("Failed to serialize " + object.getClass().getSimpleName(), e);
        }
    }

    private <T> T readResponse(Response<T> response) throws ComputeEngineClientException {
        if (response == null) {
            throw new ComputeEngineClientException("Received a null response from ComputeEngine");
        }

        if (response.getErrorMessage() != null) {
            throw new ComputeEngineClientException(
                    "Received an error from ComputeEngine.\n" + response.getErrorMessage());
        }

        ByteArrayInputStream bis = new ByteArrayInputStream(response.getBinaryObject().getBytes());
        try (ObjectInputStream ois = new ObjectInputStream(bis)) {
            return (T) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            throw new ComputeEngineClientException("Failed to deserialize a result", e);
        }
    }

}
