package ru.isma.next.app.views.koin

import org.koin.dsl.module
import ru.isma.next.app.services.editors.TextEditorFactory
import ru.isma.next.app.views.MainView
import ru.isma.next.app.views.settings.*
import ru.isma.next.app.views.tabpane.IsmaEditorTabPane
import ru.isma.next.app.views.toolbars.*
import ru.isma.next.editor.blueprint.services.ITextEditorFactory
import ru.isma.next.editor.text.IsmaTextEditor
import ru.isma.next.editor.text.services.LismaHighlightingService
import ru.isma.next.editor.text.services.contracts.IHighlightingService

val lismaTextEditorModule = module {
    factory { IsmaTextEditor(get(), get()) }
    single<IHighlightingService> { LismaHighlightingService() }
}

val blueprintEditorModule = module {
    single<ITextEditorFactory> { TextEditorFactory() }
}

val toolbarsModule = module {
    single { IsmaMenuBar(get(),get(),get(),get(),get()) }
    single { IsmaToolBar(get(),get(),get(),get(),get()) }
    single { SimulationProcessBar(get(), get()) }
    single { IsmaErrorListTable(get()) }
    factory { TasksPopOver(get(), get()) }
}

val editorTabPaneModule = module {
    single { IsmaEditorTabPane(get()) }
}

val settingsPanelModule = module {
    single { CauchyInitialsView(get()) }
    single { EventDetectionView(get()) }
    single { MethodSettingsView(get()) }
    single { ResultProcessingView(get()) }
    single { SettingsPanelView(get(),get(),get(),get()) }
}

val mainViewModule = module {
    single { MainView(get(), get(), get(), get(), get(), get()) }
}