package ru.isma.next.app.launcher

import org.koin.core.context.GlobalContext.startKoin
import org.koin.dsl.module
import ru.isma.next.app.services.koin.*
import ru.isma.next.app.views.koin.*
fun ismaKoinStart() = startKoin {
    modules(
        simulationServerModule,
        appServicesModule,
    )

    modules(
        grinProcessLauncherModule,
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

val grinProcessLauncherModule = module {
    single { GrinProcessLauncher() }
}