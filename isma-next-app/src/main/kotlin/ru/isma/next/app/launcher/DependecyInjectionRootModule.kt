package ru.isma.next.app.launcher

import org.koin.core.context.GlobalContext.startKoin
import ru.isma.next.app.services.koin.addAppServices
import ru.isma.next.app.services.koin.addExternalServices
import ru.isma.next.app.services.koin.addSimulationRunners

fun ismaKoinStart() = startKoin {
    addAppServices()
    addExternalServices()
    addSimulationRunners()
}