package ru.nstu.isma.daemon.infrastructure

import org.koin.dsl.module
import ru.nstu.isma.domain.compiler.ICompiledModelStore
import ru.nstu.isma.domain.compiler.ILismaCompiler
import ru.nstu.isma.domain.integration.IIntegrationMethodsStore
import ru.nstu.isma.domain.simulation.ISimulationExecutor
import ru.nstu.isma.domain.simulation.ISimulationProcessRegistry
import ru.nstu.isma.domain.simulation.ISimulationSessionStore
import ru.nstu.isma.daemon.infrastructure.stores.compiledModels.CompiledModelStore
import ru.nstu.isma.daemon.infrastructure.stores.integrationMethods.IntegrationMethodsStore
import ru.nstu.isma.daemon.infrastructure.stores.simulationSessions.SimulationSessionStore
import ru.nstu.isma.daemon.infrastructure.simulation.SimulationExecutorImpl
import ru.nstu.isma.daemon.infrastructure.simulation.SimulationProcessRegistry
import ru.nstu.isma.daemon.infrastructure.translation.CliLismaCompiler
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

val infrastructureModule = module {
    single<IIntegrationMethodsStore> { IntegrationMethodsStore() }
    single<ISimulationSessionStore> { SimulationSessionStore() }
    single<ICompiledModelStore> { CompiledModelStore() }
    single<ISimulationProcessRegistry> { SimulationProcessRegistry() }
    single<ILismaCompiler> { CliLismaCompiler() }
    single<ExecutorService> { Executors.newCachedThreadPool() }
    single<ISimulationExecutor> {
        SimulationExecutorImpl(
            sessionStore = get(),
            processRegistry = get(),
            executorService = get(),
        )
    }
}
