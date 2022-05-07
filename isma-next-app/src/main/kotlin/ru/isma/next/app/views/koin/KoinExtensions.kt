package ru.isma.next.app.views.koin

import javafx.scene.Node
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.scopedOf
import org.koin.core.qualifier.named
import org.koin.dsl.module
import ru.isma.next.app.models.projects.BlueprintProjectDataProvider
import ru.isma.next.app.models.projects.BlueprintProjectModel
import ru.isma.next.app.models.projects.LismaProjectDataProvider
import ru.isma.next.app.models.projects.LismaProjectModel
import ru.isma.next.app.services.editors.TextEditorFactory
import ru.isma.next.app.views.MainView
import ru.isma.next.app.views.settings.*
import ru.isma.next.app.views.tabpane.IsmaEditorTabPane
import ru.isma.next.app.views.toolbars.*
import ru.isma.next.editor.blueprint.IsmaBlueprintEditor
import ru.isma.next.editor.blueprint.services.ITextEditorFactory
import ru.isma.next.editor.text.IsmaTextEditor
import ru.isma.next.editor.text.services.LismaHighlightingService
import ru.isma.next.editor.text.services.contracts.IHighlightingService

class IsmaEditorQualifier

val editorModule = module {
    single<IHighlightingService> { LismaHighlightingService() }
}

val lismaTextEditorModule = module {
    includes(editorModule)

    scope<LismaProjectModel> {
        scopedOf(::LismaProjectDataProvider)
        scopedOf(::IsmaTextEditor)
        scoped<Node>(named<IsmaEditorQualifier>()) { get<IsmaTextEditor>() }
    }
}

val blueprintEditorModule = module {
    includes(editorModule)

    scope<BlueprintProjectModel> {
        scopedOf<ITextEditorFactory>(::TextEditorFactory)
        factoryOf(::IsmaTextEditor)
        scopedOf(::BlueprintProjectDataProvider)
        scopedOf(::IsmaBlueprintEditor)
        scoped<Node>(named<IsmaEditorQualifier>()) { get<IsmaBlueprintEditor>() }
    }
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