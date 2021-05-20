import org.koin.core.context.GlobalContext.startKoin
import services.external.externalServicesModule
import services.servicesModule

fun ismaKoinStart() = startKoin {
    modules(servicesModule)
    modules(externalServicesModule)
}