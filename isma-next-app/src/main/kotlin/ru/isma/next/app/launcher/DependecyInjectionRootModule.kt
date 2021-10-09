package ru.isma.next.app.launcher

import org.koin.core.context.GlobalContext.startKoin
import ru.isma.next.app.services.koin.*
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
    addEventDetectionServices()
}