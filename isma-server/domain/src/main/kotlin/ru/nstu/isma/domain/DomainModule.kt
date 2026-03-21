package ru.nstu.isma.domain

import org.koin.dsl.module
import ru.nstu.isma.domain.handlers.getSimulationResult.GetSimulationResultHandlerImpl
import ru.nstu.isma.domain.handlers.getSimulationResult.IGetSimulationResultHandler
import ru.nstu.isma.domain.handlers.listSimulationMethods.IListSimulationMethodsHandler
import ru.nstu.isma.domain.handlers.listSimulationMethods.ListSimulationMethodsHandlerImpl
import ru.nstu.isma.domain.handlers.monitorSimulation.IMonitorSimulationHandler
import ru.nstu.isma.domain.handlers.monitorSimulation.MonitorSimulationHandlerImpl
import ru.nstu.isma.domain.handlers.runSimulation.IRunSimulationHandler
import ru.nstu.isma.domain.handlers.runSimulation.RunSimulationHandlerImpl

val domainModule = module {
    single<IRunSimulationHandler> { RunSimulationHandlerImpl() }
    single<IGetSimulationResultHandler> { GetSimulationResultHandlerImpl() }
    single<IMonitorSimulationHandler> { MonitorSimulationHandlerImpl() }
    single<IListSimulationMethodsHandler> { ListSimulationMethodsHandlerImpl() }
}