package views

import org.koin.dsl.module
import views.editors.tabpane.IsmaEditorTabPane
import views.editors.text.IsmaTextEditor
import views.simulation.settings.SettingsPanelView
import views.toolbars.IsmaErrorListTable
import views.toolbars.IsmaMenuBar
import views.toolbars.IsmaToolBar
import views.toolbars.SimulationProcessBar

val viewsModule = module {
    single { IsmaMenuBar(get(), get(), get(), get()) }
    single { IsmaToolBar(get(), get(), get(), get(), get()) }
    single { SimulationProcessBar(get(), get()) }
    single { IsmaErrorListTable(get()) }
    single { SettingsPanelView() }
    factory { IsmaTextEditor() }
}