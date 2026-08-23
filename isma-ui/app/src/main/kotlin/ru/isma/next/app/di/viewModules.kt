package ru.isma.next.app.di

import org.koin.dsl.module
import ru.isma.next.app.services.editors.SyntaxHighlighterService
import ru.isma.next.app.services.project.ProjectEditorPort
import ru.isma.next.app.views.MainView
import ru.isma.next.app.views.editors.ProjectEditorPortImpl
import ru.isma.next.app.views.settings.*
import ru.isma.next.app.views.tabpane.IsmaEditorTabPane
import ru.isma.next.app.views.layout.ErrorListDrawer
import ru.isma.next.app.views.layout.IsmaErrorListTable
import ru.isma.next.app.views.toolbars.*
import ru.isma.next.editor.text.services.RemoteLismaHighlightingService
import ru.isma.next.editor.text.services.contracts.IHighlightingService
import ru.isma.next.editor.text.services.contracts.ISyntaxHighlighter

val editorModule = module {
    single<ISyntaxHighlighter> { SyntaxHighlighterService(get()) }
    single<IHighlightingService> { RemoteLismaHighlightingService(get()) }
}

val editorPortModule = module {
    includes(editorModule)
    single<ProjectEditorPort> { ProjectEditorPortImpl(get(), get(), get()) }
}

val toolbarsModule = module {
    single { IsmaMenuBar(get()) }
    single { IsmaToolBar(get()) }
    single { SimulationProcessBar(get(), get()) }
    single { IsmaErrorListTable(get()) }
    single { ErrorListDrawer(get()) }
    single { TasksPopOver(get()) }
}

val editorTabPaneModule = module {
    single { IsmaEditorTabPane(get()) }
}

val settingsPanelModule = module {
    single { CauchyInitialsView(get()) }
    single { EventDetectionView(get()) }
    single { MethodSettingsView(get()) }
    single { ResultSavingView(get()) }
    single { SettingsPanelView(get(), get(), get(), get()) }
}

val mainViewModule = module {
    single { MainView(get(), get(), get(), get(), get(), get()) }
}
