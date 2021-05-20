package services

import org.koin.dsl.module
import ru.nstu.grin.integration.IntegrationController

val servicesModule = module {
    single<ProjectService> { ProjectService() }
    single<FileService> { FileService(get()) }
    single<SyntaxErrorService> { SyntaxErrorService() }
    single<LismaPdeService> { LismaPdeService(get(), get()) }
    single<SyntaxHighlightingService> { SyntaxHighlightingService(get()) }
    single<SimulationParametersService> { SimulationParametersService(get()) }
    single<SimulationResultService> { SimulationResultService(get()) }
    single<SimulationService> { SimulationService(get(), get(), get(), get()) }
}