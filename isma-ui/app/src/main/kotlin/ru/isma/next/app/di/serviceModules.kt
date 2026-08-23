package ru.isma.next.app.di

import org.koin.dsl.module
import org.koin.dsl.onClose
import ru.isma.javafx.extensions.coroutines.JavaFxUiThreadExecutor
import ru.isma.javafx.extensions.coroutines.UiThreadExecutor
import ru.isma.next.app.constants.APPLICATION_PREFERENCES_FILE
import ru.isma.next.app.services.ModelErrorService
import ru.isma.next.app.services.preferences.PreferencesProvider
import ru.isma.next.app.services.project.LismaPdeService
import ru.isma.next.app.services.project.ProjectFileService
import ru.isma.next.app.services.project.IProjectService
import ru.isma.next.app.services.project.ProjectService
import ru.isma.next.app.services.simulation.ISimulationService
import ru.isma.next.app.services.simulation.ISimulationTaskService
import ru.isma.next.app.services.simulation.SimulationParametersService
import ru.isma.next.app.services.simulation.SimulationResultService
import ru.isma.next.app.services.simulation.SimulationService
import ru.isma.next.app.services.simulation.SimulationTaskService
import ru.isma.next.editor.text.services.EditorPlatformService
import ru.isma.next.editor.text.services.contracts.IEditorPlatformService
import ru.isma.next.external.SimulationServerFacade
import ru.isma.next.external.SimulationServerManager

val simulationServerModule = module {
    single { SimulationServerManager() }
    single { SimulationServerFacade(get()) }
}

val appServicesModule = module {
    single<UiThreadExecutor> { JavaFxUiThreadExecutor() }
    single<IEditorPlatformService> { EditorPlatformService() }
    single<IProjectService> { ProjectService() }
    single<ProjectFileService> { ProjectFileService(get(), get()) }
    single<ModelErrorService> { ModelErrorService() }
    single<LismaPdeService> { LismaPdeService(get(), get()) }
    single<SimulationParametersService> { SimulationParametersService(get<SimulationServerFacade>().getSimulationMethods()) }
    single<ISimulationTaskService> { SimulationTaskService(get(), get(), get()) } onClose { it?.close() }
    single<SimulationResultService> { SimulationResultService(get(), get(), get()) } onClose { it?.close() }
    single<ISimulationService> { SimulationService(get(), get()) }
    single { PreferencesProvider(APPLICATION_PREFERENCES_FILE) }
}
