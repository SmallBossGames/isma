package ru.isma.next.app.launcher

import org.koin.core.context.GlobalContext.startKoin
import ru.isma.next.app.services.koin.addAppServices
import ru.isma.next.app.services.koin.addDaeSolversServices
import ru.isma.next.app.services.koin.addExternalServices
import ru.isma.next.app.services.koin.addSimulationRunners
import ru.isma.next.app.views.koin.*

fun ismaKoinStart() = startKoin {
    addMainView()
    addAppServices()
    addExternalServices()
    addSimulationRunners()
    addLismaTextEditor()
    addBlueprintEditor()
    addToolbars()
    addEditorTabPane()
    addSettingsPanel()
    addDaeSolversServices()
}