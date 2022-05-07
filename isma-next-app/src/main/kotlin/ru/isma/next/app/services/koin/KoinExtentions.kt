package ru.isma.next.app.services.koin

import org.koin.dsl.module
import ru.isma.next.app.constants.APPLICATION_PREFERENCES_FILE
import ru.isma.next.app.services.ModelErrorService
import ru.isma.next.app.services.preferences.PreferencesProvider
import ru.isma.next.app.services.project.LismaPdeService
import ru.isma.next.app.services.project.ProjectFileService
import ru.isma.next.app.services.project.ProjectService
import ru.isma.next.app.services.simualtion.*
import ru.isma.next.editor.text.services.EditorPlatformService
import ru.isma.next.editor.text.services.contracts.IEditorPlatformService
import ru.isma.next.services.simulation.abstractions.models.SimulationParametersModel
import ru.nstu.isma.intg.api.providers.IIntegrationMethodProvider
import ru.nstu.isma.lisma.InputTranslator
import ru.nstu.isma.lisma.LismaTranslator
import ru.nstu.isma.next.core.sim.controller.services.controllers.ISimulationCoreController
import ru.nstu.isma.next.core.sim.controller.services.controllers.SimulationCoreController
import ru.nstu.isma.next.core.sim.controller.services.eventDetection.IEventDetectorFactory
import ru.nstu.isma.next.core.sim.controller.services.hsm.HsmCompiler
import ru.nstu.isma.next.core.sim.controller.services.hsm.IHsmCompiler
import ru.nstu.isma.next.core.sim.controller.services.runners.ISimulationRunnerProvider
import ru.nstu.isma.next.core.sim.controller.services.runners.InFileSimulationRunner
import ru.nstu.isma.next.core.sim.controller.services.runners.InMemorySimulationRunner
import ru.nstu.isma.next.core.sim.controller.services.simulators.HybridSystemSimulator
import ru.nstu.isma.next.core.sim.controller.services.simulators.IHybridSystemSimulator
import ru.nstu.isma.next.core.sim.controller.services.solvers.DefaultDaeSystemStepSolverFactory
import ru.nstu.isma.next.core.sim.controller.services.solvers.IDaeSystemSolverFactory
import ru.nstu.isma.next.core.sim.controller.services.solvers.RemoteDaeSystemStepSolverFactory
import ru.nstu.isma.next.integration.services.IntegrationMethodLibraryLoader

class SimulationScope

val externalServicesModule = module {
    single { IntegrationMethodLibraryLoader.load() }
    single<IEditorPlatformService> { EditorPlatformService() }
    single<IHsmCompiler> { HsmCompiler() }
    single<InputTranslator> { LismaTranslator() }
}

val simulationScopeModule = module {
    scope<SimulationScope> {

        scoped<IIntegrationMethodProvider> { IntegrationMethodProvider(get(), get()) }
        scoped<IDaeSystemSolverFactory> { DaeSystemStepSolverFactory(get(), get(), get(), get()) }
        scoped<IHybridSystemSimulator> { HybridSystemSimulator(get(), get()) }
        scoped<ISimulationRunnerProvider> { SimulationRunnerProvider(get(), get(), get()) }
        scoped<ISimulationCoreController> { SimulationCoreController(get(), get()) }
        scoped<IEventDetectorFactory> { EventDetectorFactory(get()) }
        scoped { InFileSimulationRunner(get()) }
        scoped { InMemorySimulationRunner(get()) }

        scoped<SimulationParametersModel> { get<SimulationParametersService>().snapshot() }
    }
}

val appServicesModule = module {
    single<ProjectService> { ProjectService() }
    single<ProjectFileService> { ProjectFileService(get()) }
    single<ModelErrorService> { ModelErrorService() }
    single<LismaPdeService> { LismaPdeService(get(), get()) }
    single<SimulationParametersService> { SimulationParametersService(get()) }
    single<SimulationResultService> { SimulationResultService(get()) }
    single<SimulationService> { SimulationService(get(), get(), get()) }
    single { PreferencesProvider(APPLICATION_PREFERENCES_FILE) }
}

val daeSystemStepSolversModule = module {
    single { DefaultDaeSystemStepSolverFactory() }
    single { RemoteDaeSystemStepSolverFactory() }
}