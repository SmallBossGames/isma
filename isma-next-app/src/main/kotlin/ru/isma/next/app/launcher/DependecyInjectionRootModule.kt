package ru.isma.next.app.launcher

import org.koin.core.context.GlobalContext.startKoin
import ru.isma.next.app.services.koin.*
import ru.isma.next.app.views.koin.*
import ru.nstu.grin.integration.grinModule

fun ismaKoinStart() = startKoin {
    modules(simulationScopeModule)
    modules(externalServicesModule)
    modules(appServicesModule)
    modules(daeSystemStepSolversModule)

    modules(grinModule)

    addMainView()
    addLismaTextEditor()
    addBlueprintEditor()
    addToolbars()
    addEditorTabPane()
    addSettingsPanel()
}