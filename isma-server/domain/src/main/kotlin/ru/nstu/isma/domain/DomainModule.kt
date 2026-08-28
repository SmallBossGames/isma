package ru.nstu.isma.domain

import org.koin.dsl.module
import ru.nstu.isma.domain.handlers.cancelSimulation.CancelSimulationHandlerImpl
import ru.nstu.isma.domain.handlers.cancelSimulation.ICancelSimulationHandler
import ru.nstu.isma.domain.handlers.compileLisma.CompileLismaHandlerImpl
import ru.nstu.isma.domain.handlers.compileLisma.ICompileLismaHandler
import ru.nstu.isma.domain.handlers.deleteCompiledModel.DeleteCompiledModelHandlerImpl
import ru.nstu.isma.domain.handlers.deleteCompiledModel.IDeleteCompiledModelHandler
import ru.nstu.isma.domain.handlers.getSimulationResult.GetSimulationResultHandlerImpl
import ru.nstu.isma.domain.handlers.getSimulationResult.IGetSimulationResultHandler
import ru.nstu.isma.domain.handlers.highlightLisma.IHighlightLismaHandler
import ru.nstu.isma.domain.handlers.listSimulationMethods.IListSimulationMethodsHandler
import ru.nstu.isma.domain.handlers.listSimulationMethods.ListSimulationMethodsHandlerImpl
import ru.nstu.isma.domain.handlers.monitorSimulation.IMonitorSimulationHandler
import ru.nstu.isma.domain.handlers.monitorSimulation.MonitorSimulationHandlerImpl
import ru.nstu.isma.domain.handlers.runSimulation.IRunSimulationHandler
import ru.nstu.isma.domain.handlers.runSimulation.RunSimulationHandlerImpl
import ru.nstu.isma.domain.handlers.validateLisma.IValidateLismaHandler
import ru.nstu.isma.domain.handlers.validateLisma.ValidateLismaHandlerImpl

val domainModule = module {
    single<IRunSimulationHandler> { RunSimulationHandlerImpl(get(), get(), get()) }
    single<IGetSimulationResultHandler> { GetSimulationResultHandlerImpl(get()) }
    single<IMonitorSimulationHandler> { MonitorSimulationHandlerImpl(get()) }
    single<IListSimulationMethodsHandler> { ListSimulationMethodsHandlerImpl(get()) }
    single<ICancelSimulationHandler> { CancelSimulationHandlerImpl(get()) }
    single<ICompileLismaHandler> { CompileLismaHandlerImpl(get(), get()) }
    single<IValidateLismaHandler> { ValidateLismaHandlerImpl(get()) }
    single<IDeleteCompiledModelHandler> { DeleteCompiledModelHandlerImpl(get()) }
    single<IHighlightLismaHandler> { get<IHighlightLismaHandler>() }
}
