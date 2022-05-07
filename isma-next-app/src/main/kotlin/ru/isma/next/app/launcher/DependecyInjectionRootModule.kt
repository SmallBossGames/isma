package ru.isma.next.app.launcher

import org.koin.core.context.GlobalContext.startKoin
import ru.isma.next.app.models.koin.projectsModule
import ru.isma.next.app.services.koin.*
import ru.isma.next.app.views.koin.*
import ru.nstu.grin.integration.grinIntegrationModule
import ru.nstu.grin.integration.grinGuiModule

fun ismaKoinStart() = startKoin {
    modules(
        simulationScopeModule,
        externalServicesModule,
        appServicesModule,
        daeSystemStepSolversModule
    )

    modules(
        grinGuiModule,
        grinIntegrationModule,
    )

    modules(
        toolbarsModule,
        mainViewModule,
        settingsPanelModule,
        editorTabPaneModule,
        lismaTextEditorModule,
        blueprintEditorModule
    )

    modules(
        projectsModule
    )
}