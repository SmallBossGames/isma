package ru.nstu.isma.daemon.app

import org.koin.dsl.module
import ru.nstu.isma.daemon.app.grpc.LismaCompilerServiceGrpcImpl
import ru.nstu.isma.daemon.app.grpc.SimulationServiceGrpcImpl

val appModule = module {
    single { SimulationServiceGrpcImpl(get(), get(), get(), get(), get()) }
    single { LismaCompilerServiceGrpcImpl(get(), get(), get()) }
}