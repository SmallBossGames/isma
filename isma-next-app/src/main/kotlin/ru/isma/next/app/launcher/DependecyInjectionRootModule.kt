package ru.isma.next.app.launcher

import org.koin.core.context.GlobalContext.startKoin
import ru.isma.next.app.services.external.externalServicesModule
import ru.isma.next.app.services.servicesModule

fun ismaKoinStart() = startKoin {
    modules(servicesModule)
    modules(externalServicesModule)
}