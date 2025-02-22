package ru.isma.next.app.launcher

import org.koin.core.context.GlobalContext.startKoin
import ru.isma.next.app.services.koin.*
import ru.isma.next.app.views.koin.*
import ru.nstu.grin.integration.grinNestedModule

fun ismaKoinStart() = startKoin {
    modules(
        simulationScopeModule,
        externalServicesModule,
        appServicesModule,
    )

    modules(
        grinNestedModule,
    )

    modules(
        toolbarsModule,
        mainViewModule,
        settingsPanelModule,
        editorTabPaneModule,
        lismaTextEditorModule,
        blueprintEditorModule,
    )
}