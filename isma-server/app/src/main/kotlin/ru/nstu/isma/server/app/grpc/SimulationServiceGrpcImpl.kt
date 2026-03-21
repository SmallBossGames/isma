package ru.nstu.isma.server.app.grpc

import com.google.protobuf.ByteString
import io.grpc.stub.StreamObserver
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

    override fun runSimulation(
        request: RunSimulationRequest,
        responseObserver: StreamObserver<RunSimulationResponse>
    ) {
        try {
            if (request.lismaSourceCode.isBlank()) {
                responseObserver.onError(
                    IllegalArgumentException("LISMA source code is required")
                )
                return
            }

            val parameters = RunSimulationParameters(
                startTime = request.startTime,
                endTime = request.endTime,
                initialStep = request.initialStep,
                methodName = request.methodName,
                accuracy = request.accuracy,
                isAccuracyInUse = request.isAccuracyInUse,
                lismaSourceCode = request.lismaSourceCode,
            )
            val result = runSimulationHandler.handle(parameters)
            responseObserver.onNext(
                RunSimulationResponse.newBuilder()
                    .setSimulationId(result.simulationId)
                    .build()
            )
            responseObserver.onCompleted()
        } catch (e: Exception) {
            responseObserver.onError(e)
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
            responseObserver.onError(e)
        }
    }

    override fun monitorSimulation(
        request: MonitorSimulationRequest,
        responseObserver: StreamObserver<MonitorSimulationResponse>
    ) {
        try {
            monitorSimulationHandler.handle(request.simulationId) { progress ->
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
            responseObserver.onError(e)
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
            responseObserver.onError(e)
        }
    }
}
