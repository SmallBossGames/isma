package ru.nstu.isma.server.app.grpc

import com.google.protobuf.ByteString
import io.grpc.Status
import io.grpc.StatusException
import io.grpc.stub.StreamObserver
import org.slf4j.LoggerFactory
import ru.nstu.isma.contracts.simulation.*
import ru.nstu.isma.domain.handlers.getSimulationResult.IGetSimulationResultHandler
import ru.nstu.isma.domain.handlers.listSimulationMethods.IListSimulationMethodsHandler
import ru.nstu.isma.domain.handlers.monitorSimulation.IMonitorSimulationHandler
import ru.nstu.isma.domain.handlers.runSimulation.IRunSimulationHandler
import ru.nstu.isma.domain.handlers.runSimulation.RunSimulationParameters

class SimulationServiceGrpcImpl(
    private val runSimulationHandler: IRunSimulationHandler,
    private val getSimulationResultHandler: IGetSimulationResultHandler,
    private val monitorSimulationHandler: IMonitorSimulationHandler,
    private val listSimulationMethodsHandler: IListSimulationMethodsHandler,
) : SimulationServiceGrpc.SimulationServiceImplBase() {

    private val logger = LoggerFactory.getLogger(SimulationServiceGrpcImpl::class.java)

    override fun runSimulation(
        request: RunSimulationRequest,
        responseObserver: StreamObserver<RunSimulationResponse>
    ) {
        try {
            if (request.lismaSourceCode.isBlank()) {
                responseObserver.onError(
                    Status.INVALID_ARGUMENT.withDescription("LISMA source code is required").asException()
                )
                return
            }

            val parameters = RunSimulationParameters(
                startTime = request.startTime,
                endTime = request.endTime,
                initialStep = request.initialStep,
                methodName = request.methodName,
                accuracy = if (request.hasAccuracyConfig()) request.accuracyConfig.accuracy else 0.0,
                isAccuracyInUse = request.hasAccuracyConfig(),
                isStabilityControlInUse = request.hasStabilityConfig(),
                lismaSourceCode = request.lismaSourceCode,
                eventDetectionGamma = if (request.hasEventDetection()) request.eventDetection.gamma else null,
                eventDetectionLowBorder = if (request.hasEventDetection()) request.eventDetection.lowBorder else null,
            )
            val result = runSimulationHandler.handle(parameters)
            responseObserver.onNext(
                RunSimulationResponse.newBuilder()
                    .setSimulationId(result.simulationId)
                    .build()
            )
            responseObserver.onCompleted()
        } catch (e: Exception) {
            logger.error("runSimulation failed", e)
            responseObserver.onError(toStatusException(e))
        }
    }

    override fun getSimulationResult(
        request: GetSimulationResultRequest,
        responseObserver: StreamObserver<GetSimulationResultResponse>
    ) {
        try {
            val resultStream = getSimulationResultHandler.handle(request.simulationId)
            val bytes = resultStream.readAllBytes()
            responseObserver.onNext(
                GetSimulationResultResponse.newBuilder()
                    .setResultData(ByteString.copyFrom(bytes))
                    .build()
            )
            responseObserver.onCompleted()
        } catch (e: Exception) {
            logger.error("getSimulationResult failed for simulationId=${request.simulationId}", e)
            responseObserver.onError(toStatusException(e))
        }
    }

    override fun monitorSimulation(
        request: MonitorSimulationRequest,
        responseObserver: StreamObserver<MonitorSimulationResponse>
    ) {
        try {
            monitorSimulationHandler.handle(request.simulationId, request.accuracy) { progress ->
                responseObserver.onNext(
                    MonitorSimulationResponse.newBuilder()
                        .setStartTime(progress.startTime)
                        .setEndTime(progress.endTime)
                        .setCurrentTime(progress.currentTime)
                        .build()
                )
            }
            responseObserver.onCompleted()
        } catch (e: Exception) {
            logger.error("monitorSimulation failed for simulationId=${request.simulationId}", e)
            responseObserver.onError(toStatusException(e))
        }
    }

    override fun listSimulationMethods(
        request: ListSimulationMethodsRequest,
        responseObserver: StreamObserver<ListSimulationMethodsResponse>
    ) {
        try {
            val methods = listSimulationMethodsHandler.handle()
            val responseBuilder = ListSimulationMethodsResponse.newBuilder()
            methods.forEach { item ->
                responseBuilder.addMethods(
                    SimulationMethodItem.newBuilder()
                        .setName(item.name)
                        .setTitle(item.title)
                        .build()
                )
            }
            responseObserver.onNext(responseBuilder.build())
            responseObserver.onCompleted()
        } catch (e: Exception) {
            logger.error("listSimulationMethods failed", e)
            responseObserver.onError(toStatusException(e))
        }
    }

    private fun toStatusException(e: Exception): StatusException {
        return when (e) {
            is IllegalArgumentException -> Status.NOT_FOUND.withDescription(e.message).asException()
            is IllegalStateException -> Status.FAILED_PRECONDITION.withDescription(e.message).asException()
            else -> Status.INTERNAL.withDescription(e.message).asException()
        }
    }
}
