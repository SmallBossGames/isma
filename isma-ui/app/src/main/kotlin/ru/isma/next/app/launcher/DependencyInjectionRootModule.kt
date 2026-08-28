package ru.isma.next.app.launcher

import org.koin.core.context.GlobalContext.startKoin
import org.koin.dsl.module
import ru.isma.next.app.di.appServicesModule
import ru.isma.next.app.di.editorPortModule
import ru.isma.next.app.di.editorTabPaneModule
import ru.isma.next.app.di.mainViewModule
import ru.isma.next.app.di.settingsPanelModule
import ru.isma.next.app.di.simulationServerModule
import ru.isma.next.app.di.toolbarsModule
import ru.isma.next.app.di.viewModelsModule

fun ismaKoinStart() = startKoin {
    modules(
        simulationServerModule,
        appServicesModule,
    )

    modules(
        grinProcessLauncherModule,
    )

    modules(
        viewModelsModule,
        editorPortModule,
        toolbarsModule,
        mainViewModule,
        settingsPanelModule,
        editorTabPaneModule,
    )
}

val grinProcessLauncherModule = module {
    single { GrinProcessLauncher() }
}
