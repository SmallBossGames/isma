package ru.isma.next.app.views.koin

import javafx.scene.control.TabPane
import org.koin.core.KoinApplication
import org.koin.dsl.module
import ru.isma.next.app.services.editors.TextEditorFactory
import ru.isma.next.app.views.MainView
import ru.isma.next.app.views.settings.*
import ru.isma.next.app.views.tabpane.IsmaEditorTabPane
import ru.isma.next.app.views.toolbars.IsmaErrorListTable
import ru.isma.next.app.views.toolbars.IsmaMenuBar
import ru.isma.next.app.views.toolbars.IsmaToolBar
import ru.isma.next.app.views.toolbars.SimulationProcessBar
import ru.isma.next.editor.blueprint.IsmaBlueprintEditor
import ru.isma.next.editor.blueprint.services.ITextEditorFactory
import ru.isma.next.editor.text.IsmaTextEditor
import ru.isma.next.editor.text.services.LismaHighlightingService
import ru.isma.next.editor.text.services.contracts.IHighlightingService

fun KoinApplication.addLismaTextEditor() {
    val module = module {
        factory { IsmaTextEditor(get(), get()) }
        single<IHighlightingService> { LismaHighlightingService() }
    }
    modules(module)
}

fun KoinApplication.addBlueprintEditor() {
    val module = module {
        factory { IsmaBlueprintEditor(get()) }
        single<ITextEditorFactory> { TextEditorFactory() }
    }
    modules(module)
}

fun KoinApplication.addToolbars() {
    val module = module {
        single { IsmaMenuBar(get(),get(),get(),get(),get()) }
        single { IsmaToolBar(get(),get(),get(),get(),get()) }
        single { SimulationProcessBar(get(),get()) }
        single { IsmaErrorListTable(get()) }
    }
    modules(module)
}

fun KoinApplication.addEditorTabPane() {
    modules(
        module {
            single { IsmaEditorTabPane(get()) }
        }
    )
}

fun KoinApplication.addSettingsPanel(){
    modules(
        module {
            single { CauchyInitialsView(get()) }
            single { EventDetectionView(get()) }
            single { MethodSettingsView(get()) }
            single { ResultProcessingView(get()) }
            single { SettingsPanelView(get(),get(),get(),get()) }
        }
    )
}

fun KoinApplication.addMainView() {
    modules(
        module {
            single { MainView(get(),get(),get(),get(),get(),get(),get(),get()) }
        }
    )
}