package ru.isma.next.app.launcher

import org.koin.core.context.GlobalContext.startKoin
import org.koin.dsl.module
import ru.isma.next.app.di.appServicesModule
import ru.isma.next.app.di.blueprintEditorModule
import ru.isma.next.app.di.editorTabPaneModule
import ru.isma.next.app.di.lismaTextEditorModule
import ru.isma.next.app.di.mainViewModule
import ru.isma.next.app.di.settingsPanelModule
import ru.isma.next.app.di.simulationServerModule
import ru.isma.next.app.di.toolbarsModule
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