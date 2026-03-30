package ru.nstu.isma.server.infrastructure

import org.koin.dsl.module
import ru.nstu.isma.domain.compiler.ICompiledModelStore
import ru.nstu.isma.domain.handlers.highlightLisma.IHighlightLismaHandler
import ru.nstu.isma.domain.handlers.runSimulation.ILismaTranslator
import ru.nstu.isma.domain.integration.IIntegrationMethodsStore
import ru.nstu.isma.domain.simulation.ISimulationExecutor
import ru.nstu.isma.domain.simulation.ISimulationSessionStore
import ru.nstu.isma.lisma.InputTranslator
import ru.nstu.isma.lisma.LismaTranslator
import ru.nstu.isma.next.core.sim.controller.services.hsm.HsmCompiler
import ru.nstu.isma.next.core.sim.controller.services.hsm.IHsmCompiler
import ru.nstu.isma.next.integration.services.IntegrationMethodLibraryLoader
import ru.nstu.isma.next.integration.services.IntegrationMethodsLibrary
import ru.nstu.isma.server.infrastructure.highlight.HighlightLismaHandlerImpl
import ru.nstu.isma.server.infrastructure.stores.compiledModels.CompiledModelStore
import ru.nstu.isma.server.infrastructure.stores.integrationMethods.IntegrationMethodsStore
import ru.nstu.isma.server.infrastructure.stores.simulationSessions.SimulationSessionStore
import ru.nstu.isma.server.infrastructure.simulation.SimulationExecutorImpl
import ru.nstu.isma.server.infrastructure.translation.LismaTranslatorImpl
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

val infrastructureModule = module {
    single<IIntegrationMethodsStore> { IntegrationMethodsStore() }
    single<ISimulationSessionStore> { SimulationSessionStore() }
    single<ICompiledModelStore> { CompiledModelStore() }
    single<InputTranslator> { LismaTranslator() }
    single<ILismaTranslator> { LismaTranslatorImpl(get()) }
    single<IHsmCompiler> { HsmCompiler() }
    single<IntegrationMethodsLibrary> { IntegrationMethodLibraryLoader.load() }
    single<ExecutorService> { Executors.newCachedThreadPool() }
    single<ISimulationExecutor> {
        SimulationExecutorImpl(
            integrationMethodsLibrary = get(),
            hsmCompiler = get(),
            sessionStore = get(),
            executorService = get(),
        )
    }
    single<IHighlightLismaHandler> { HighlightLismaHandlerImpl() }
}
