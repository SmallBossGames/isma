import org.koin.core.context.GlobalContext.startKoin
import services.external.externalServicesModule
import services.servicesModule
import views.viewsModule

fun ismaKoinStart() = startKoin {
    modules(servicesModule)
    modules(externalServicesModule)
    modules(viewsModule)
}