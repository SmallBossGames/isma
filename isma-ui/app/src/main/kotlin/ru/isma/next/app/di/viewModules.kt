package ru.isma.next.app.di

import javafx.scene.Node
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.scopedOf
import org.koin.core.qualifier.named
import org.koin.dsl.module
import org.koin.dsl.onClose
import ru.isma.next.app.models.projects.BlueprintProjectDataProvider
import ru.isma.next.app.models.projects.BlueprintProjectModel
import ru.isma.next.app.models.projects.LismaProjectDataProvider
import ru.isma.next.app.models.projects.LismaProjectModel
import ru.isma.next.app.services.editors.SyntaxHighlighterService
import ru.isma.next.app.services.editors.TextEditorFactory
import ru.isma.next.app.views.MainView
import ru.isma.next.app.views.settings.*
import ru.isma.next.app.views.tabpane.IsmaEditorTabPane
import ru.isma.next.app.views.layout.ErrorListDrawer
import ru.isma.next.app.views.toolbars.*
import ru.isma.next.editor.blueprint.views.IsmaBlueprintEditor
import ru.isma.next.editor.blueprint.services.ITextEditorFactory
import ru.isma.next.editor.text.IsmaTextEditor
import ru.isma.next.editor.text.services.RemoteLismaHighlightingService
import ru.isma.next.editor.text.services.contracts.IHighlightingService
import ru.isma.next.editor.text.services.contracts.ISyntaxHighlighter

class IsmaEditorQualifier

val editorModule = module {
    single<ISyntaxHighlighter> { SyntaxHighlighterService(get()) }
    single<IHighlightingService> { RemoteLismaHighlightingService(get()) }
}

val lismaTextEditorModule = module {
    includes(editorModule)

    scope<LismaProjectModel> {
        scopedOf(::LismaProjectDataProvider)
        scopedOf(::IsmaTextEditor) onClose { it?.dispose() }
        scoped<Node>(named<IsmaEditorQualifier>()) { get<IsmaTextEditor>() }
    }
}

val blueprintEditorModule = module {
    includes(editorModule)

    scope<BlueprintProjectModel> {
        scoped<ITextEditorFactory> { TextEditorFactory { get() } }
        factoryOf(::IsmaTextEditor) onClose { it?.dispose() }
        scopedOf(::BlueprintProjectDataProvider)
        scopedOf(::IsmaBlueprintEditor)
        scoped<Node>(named<IsmaEditorQualifier>()) { get<IsmaBlueprintEditor>() }
    }
}

val toolbarsModule = module {
    single { IsmaMenuBar(get(), get(), get(), get(), get()) }
    single { IsmaToolBar(get(), get(), get(), get(), get()) }
    single { SimulationProcessBar(get(), get()) }
    single { IsmaErrorListTable(get()) }
    single { ErrorListDrawer(get()) }
    factory { TasksPopOver(get(), get()) }
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
