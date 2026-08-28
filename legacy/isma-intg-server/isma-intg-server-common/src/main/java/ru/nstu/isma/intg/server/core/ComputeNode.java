package ru.nstu.isma.intg.server.core;

import com.google.common.base.Preconditions;
import com.google.common.base.Throwables;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.nstu.isma.intg.api.calcmodel.DaeSystem;
import ru.nstu.isma.intg.api.calcmodel.DaeSystemChangeSet;
import ru.nstu.isma.intg.api.methods.IntegrationMethodRungeKutta;
import ru.nstu.isma.intg.api.methods.IntgPoint;
import ru.nstu.isma.intg.core.solvers.parallel.ParallelDaeSystemStepSolver;
import ru.nstu.isma.intg.server.ClassDataProvider;
import ru.nstu.isma.intg.server.models.*;
import ru.nstu.isma.intg.server.services.MpiService;
import ru.nstu.isma.intg.server.services.ServiceFacade;
import ru.nstu.isma.intg.server.utils.BinaryObjectDeserializer;
import ru.nstu.isma.intg.server.utils.RemoteClassLoader;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ComputeNode {

    private final Logger logger = LoggerFactory.getLogger(ComputeNode.class);
    private final ServiceFacade serviceFacade;

    private IntegrationMethodRungeKutta intgMethod;
    private DaeSystem daeSystem;
    private ParallelDaeSystemStepSolver stepSolver;
    private boolean recreateStepSolver = true;
    private final Map<String, byte[]> cachedProvidedClassData = new HashMap<>();

    private final ClassDataProvider classDataProvider = cachedProvidedClassData::get;
    private RemoteClassLoader remoteClassLoader =
            new RemoteClassLoader(this.getClass().getClassLoader(), classDataProvider);

    public ComputeNode(ServiceFacade serviceFacade) {
        this.serviceFacade = serviceFacade;
    }

    public ParallelDaeSystemStepSolver getStepSolver() {
        if (stepSolver == null || recreateStepSolver) {
            recreateStepSolver = false;
            stepSolver = new ParallelDaeSystemStepSolver(intgMethod, daeSystem);
        }
        return stepSolver;
    }

    public void start(String[] args) {
        MpiService mpiService = serviceFacade.getMpiService();
        mpiService.initMpi(args);
        logger.info("Started compute node #{}", mpiService.getRank());

        serviceFacade.getMessageService().addMessageListener(MessageType.CLEAR_RESOURCES, message -> {
            intgMethod = null;
            daeSystem = null;
            stepSolver = null;
            recreateStepSolver = true;
            cachedProvidedClassData.clear();
            if (remoteClassLoader != null) {
                remoteClassLoader.getClassDataProviderRef().clear();
            }
            remoteClassLoader = new RemoteClassLoader(this.getClass().getClassLoader(), classDataProvider);
        });

        serviceFacade.getMessageService().addMessageListener(MessageType.LOAD_INTG_METHOD, message -> {
            var clientRequest = (ClientRequest<IntegrationMethodRungeKutta>) message.getPayload();
            intgMethod = deserialize(IntegrationMethodRungeKutta.class,
                    clientRequest.getRequest().getBinaryObject().getBytes(),
                    clientRequest.getSerializedClasses());
            Preconditions.checkNotNull(intgMethod, "intgMethod cannot be null");
            recreateStepSolver = true;
        });

        serviceFacade.getMessageService().addMessageListener(MessageType.LOAD_DAE_SYSTEM, message -> {
            ClientRequest<DaeSystem> clientRequest = (ClientRequest<DaeSystem>) message.getPayload();
            daeSystem = deserialize(DaeSystem.class,
                    clientRequest.getRequest().getBinaryObject().getBytes(),
                    clientRequest.getSerializedClasses());
            Preconditions.checkNotNull(daeSystem, "daeSystem cannot be null");
            recreateStepSolver = true;
        });

        serviceFacade.getMessageService().addMessageListener(MessageType.CALCULATE_RHS, message -> {
            ClientRequest<double[]> clientRequest = (ClientRequest<double[]>) message.getPayload();

            double[] yForDe = deserialize(double[].class,
                    clientRequest.getRequest().getBinaryObject().getBytes(),
                    clientRequest.getSerializedClasses());

            double[][] rhs = getStepSolver().calculateRhs(yForDe);

            if (serviceFacade.getMpiService().isMasterNode()) {
                CalculateRhsResponse response;
                try {
                    response = CalculateRhsResponse.create(rhs);
                } catch (IOException e) {
                    logger.error("Failed to serialize CalculateRhsResponse", e);
                    throw Throwables.propagate(e);
                }
                serviceFacade.getMpiService()
                        .sendMessageToMaster(new Message(MessageType.CALCULATE_RHS_RESPONSE, response));
            }
        });

        serviceFacade.getMessageService().addMessageListener(MessageType.APPLY, message -> {
            ClientRequest<DaeSystemChangeSet> clientRequest = (ClientRequest<DaeSystemChangeSet>) message
                    .getPayload();
            DaeSystemChangeSet changeSet = deserialize(DaeSystemChangeSet.class,
                    clientRequest.getRequest().getBinaryObject().getBytes(),
                    clientRequest.getSerializedClasses());
            getStepSolver().apply(changeSet);
        });

        serviceFacade.getMessageService().addMessageListener(MessageType.STEP, message -> {
            logger.debug("Node #{} started processing STEP message...", mpiService.getRank());

            ClientRequest<IntgPoint> clientRequest = (ClientRequest<IntgPoint>) message.getPayload();

            IntgPoint fromPoint = deserialize(IntgPoint.class,
                    clientRequest.getRequest().getBinaryObject().getBytes(),
                    clientRequest.getSerializedClasses());

            serviceFacade.getMpiService().barrier();

            IntgPoint toPoint = getStepSolver().step(fromPoint);

            logger.debug("Node #{} finished step solving.", mpiService.getRank());

            if (serviceFacade.getMpiService().isMasterNode()) {
                StepResponse response;
                try {
                    response = StepResponse.create(toPoint);
                } catch (IOException e) {
                    logger.error("Failed to serialize IntgStepResult", e);
                    throw Throwables.propagate(e);
                }
                serviceFacade.getMpiService().sendMessageToMaster(new Message(MessageType.STEP_RESPONSE, response));
            }

        });
    }

    public void stop() {
        int rank = serviceFacade.getMpiService().getRank();

        logger.info("Stopping a compute node #{} ...", rank);
        serviceFacade.getMpiService().finalizeMpi();

        logger.info("Stopped a compute node{}", rank);
    }

    private <T> T deserialize(Class<T> objectClass, byte[] objectBytes, Map<String, byte[]> providedClasses) {
        cachedProvidedClassData.putAll(providedClasses);
        try {
            return BinaryObjectDeserializer.deserialize(objectClass, objectBytes, remoteClassLoader);
        } catch (IOException | ClassNotFoundException e) {
            throw new RuntimeException("Failed to deserialize IntgMethod", e);
        }
    }

}
