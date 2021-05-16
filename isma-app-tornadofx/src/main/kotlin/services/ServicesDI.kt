package services

import org.koin.dsl.module
import ru.nstu.grin.integration.IntegrationController

val servicesModule = module {
    single<ProjectService> { ProjectService() }
    single<FileService> { FileService(get()) }
    single<SyntaxErrorService> { SyntaxErrorService() }
    single<LismaPdeService> { LismaPdeService(get(), get()) }
    single<SyntaxHighlightingService> { SyntaxHighlightingService(get()) }
    single<SimulationParametersService> { SimulationParametersService() }
    single<SimulationResultService> { SimulationResultService(get()) }
    single<SimulationController> { SimulationController(get(), get(), get()) }
}

val externalServicesModule = module {
    single { IntegrationController() }
}