package services

import org.koin.dsl.module
import services.editor.TextEditorService
import services.lisma.LismaPdeService
import services.lisma.LismaHighlightingService
import services.project.ProjectFileService
import services.project.ProjectService
import services.simualtion.SimulationParametersService
import services.simualtion.SimulationResultService
import services.simualtion.SimulationService

val servicesModule = module {
    single<ProjectService> { ProjectService() }
    single<ProjectFileService> { ProjectFileService(get()) }
    single<ModelErrorService> { ModelErrorService() }
    single<LismaPdeService> { LismaPdeService(get()) }
    single<LismaHighlightingService> { LismaHighlightingService(get()) }
    single<SimulationParametersService> { SimulationParametersService(get()) }
    single<SimulationResultService> { SimulationResultService(get()) }
    single<SimulationService> { SimulationService(get(), get(), get(), get(), get()) }
    single<TextEditorService> { TextEditorService() }
}