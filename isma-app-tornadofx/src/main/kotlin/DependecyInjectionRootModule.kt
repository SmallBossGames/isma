import org.koin.core.context.GlobalContext.startKoin
import services.servicesModule

fun ismaKoinStart() = startKoin {
    modules(servicesModule)
}