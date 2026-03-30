package ru.nstu.isma.server.app

import org.koin.dsl.module
import ru.nstu.isma.server.app.grpc.LismaCompilerServiceGrpcImpl
import ru.nstu.isma.server.app.grpc.SimulationServiceGrpcImpl

val appModule = module {
    single { SimulationServiceGrpcImpl(get(), get(), get(), get(), get()) }
    single { LismaCompilerServiceGrpcImpl(get(), get(), get(), get()) }
}