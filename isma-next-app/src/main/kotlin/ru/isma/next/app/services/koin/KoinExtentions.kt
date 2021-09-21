package ru.isma.next.app.services.koin

import org.koin.core.KoinApplication
import org.koin.dsl.module
import ru.isma.next.app.constants.APPLICATION_PREFERENCES_FILE
import ru.isma.next.app.services.ModelErrorService
import ru.isma.next.app.services.preferences.PreferencesProvider
import ru.isma.next.app.services.project.LismaPdeService
import ru.isma.next.app.services.project.ProjectFileService
import ru.isma.next.app.services.project.ProjectService
import ru.isma.next.app.services.simualtion.*
import ru.isma.next.editor.text.services.TextEditorService
import ru.isma.next.editor.text.services.contracts.ITextEditorService
import ru.nstu.grin.integration.IntegrationController
import ru.nstu.isma.lisma.InputTranslator
import ru.nstu.isma.lisma.LismaTranslator
import ru.nstu.isma.next.core.sim.controller.HsmCompiler
import ru.nstu.isma.next.core.sim.controller.HybridSystemSimulator
import ru.nstu.isma.next.core.sim.controller.SimulationCoreController
import ru.nstu.isma.next.core.sim.controller.contracts.IHsmCompiler
import ru.nstu.isma.next.core.sim.controller.contracts.IHybridSystemSimulator
import ru.nstu.isma.next.core.sim.controller.contracts.ISimulationCoreController
import ru.nstu.isma.next.core.sim.controller.services.IIntegrationMethodProvider
import ru.nstu.isma.next.core.sim.controller.services.ISimulationRunnerProvider
import ru.nstu.isma.next.core.sim.controller.services.InFileSimulationRunner
import ru.nstu.isma.next.core.sim.controller.services.InMemorySimulationRunner
import ru.nstu.isma.next.core.sim.controller.services.solvers.DefaultDaeSystemStepSolverFactory
import ru.nstu.isma.next.core.sim.controller.services.solvers.IDaeSystemSolverFactory
import ru.nstu.isma.next.core.sim.controller.services.solvers.RemoteDaeSystemStepSolverFactory
import ru.nstu.isma.next.integration.services.IntegrationMethodLibraryLoader

fun KoinApplication.addSimulationRunners() {
    val module = module {
        single { InFileSimulationRunner(get(), "temp.csv") }
        single { InMemorySimulationRunner(get()) }
    }
    modules(module)
}

fun KoinApplication.addExternalServices() {
    val module = module {
        single { IntegrationController() }
        single { IntegrationMethodLibraryLoader().load() }
        single<ITextEditorService> { TextEditorService() }
        single<ISimulationCoreController> { SimulationCoreController(get(), get()) }

        single<IHsmCompiler> { HsmCompiler() }
        single<InputTranslator> { LismaTranslator() }
        single<IIntegrationMethodProvider> { IntegrationMethodProvider(get(), get()) }

        factory<IHybridSystemSimulator> { HybridSystemSimulator(get()) }
    }
    modules(module)
}

fun KoinApplication.addAppServices() {
    val module = module {
        single<ProjectService> { ProjectService() }
        single<ProjectFileService> { ProjectFileService(get()) }
        single<ModelErrorService> { ModelErrorService() }
        single<LismaPdeService> { LismaPdeService(get(), get()) }
        single<SimulationParametersService> { SimulationParametersService(get()) }
        single<SimulationResultService> { SimulationResultService(get()) }
        single<SimulationService> { SimulationService(get(), get(), get(), get(), get()) }
        single { PreferencesProvider(APPLICATION_PREFERENCES_FILE) }
        single<ISimulationRunnerProvider> { SimulationRunnerProvider(get(), get(), get()) }
    }
    modules(module)
}

fun KoinApplication.addDaeSolversServices() {
    val module = module {
        single<IDaeSystemSolverFactory> { DaeSystemStepSolverFactory(get(), get(), get(), get()) }
        single { DefaultDaeSystemStepSolverFactory() }
        single { RemoteDaeSystemStepSolverFactory() }
    }
    modules(module)
}