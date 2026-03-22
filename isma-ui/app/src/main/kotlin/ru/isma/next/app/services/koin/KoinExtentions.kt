package ru.isma.next.app.services.koin

import org.koin.dsl.module
import ru.isma.next.app.constants.APPLICATION_PREFERENCES_FILE
import ru.isma.next.app.services.ModelErrorService
import ru.isma.next.app.services.preferences.PreferencesProvider
import ru.isma.next.app.services.project.LismaPdeService
import ru.isma.next.app.services.project.ProjectFileService
import ru.isma.next.app.services.project.ProjectService
import ru.isma.next.app.services.simualtion.SimulationParametersService
import ru.isma.next.app.services.simualtion.SimulationResultService
import ru.isma.next.app.services.simualtion.SimulationService
import ru.isma.next.editor.text.services.EditorPlatformService
import ru.isma.next.editor.text.services.contracts.IEditorPlatformService
import ru.isma.next.external.SimulationServerFacade
import ru.isma.next.external.SimulationServerManager
import ru.nstu.isma.lisma.InputTranslator
import ru.nstu.isma.lisma.LismaTranslator
import ru.nstu.isma.next.integration.services.IntegrationMethodLibraryLoader

val simulationServerModule = module {
    single { SimulationServerManager() }
    single { SimulationServerFacade(get()) }
}

val appServicesModule = module {
    single { IntegrationMethodLibraryLoader.load() }
    single<IEditorPlatformService> { EditorPlatformService() }
    single<InputTranslator> { LismaTranslator() }
    single<ProjectService> { ProjectService() }
    single<ProjectFileService> { ProjectFileService(get()) }
    single<ModelErrorService> { ModelErrorService() }
    single<LismaPdeService> { LismaPdeService(get(), get()) }
    single<SimulationParametersService> { SimulationParametersService(get()) }
    single<SimulationResultService> { SimulationResultService(get()) }
    single<SimulationService> { SimulationService(get(), get(), get(), get()) }
    single { PreferencesProvider(APPLICATION_PREFERENCES_FILE) }
}
