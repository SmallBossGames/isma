import org.koin.core.context.GlobalContext.startKoin
import services.externalServicesModule
import services.servicesModule

fun ismaKoinStart() = startKoin {
    modules(servicesModule)
    modules(externalServicesModule)
}