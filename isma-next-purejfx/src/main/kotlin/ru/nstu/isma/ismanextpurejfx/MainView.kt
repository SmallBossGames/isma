package ru.nstu.isma.ismanextpurejfx

import javafx.scene.Parent
import javafx.scene.layout.BorderPane
import javafx.scene.layout.VBox
import javafx.scene.text.Text
import ru.nstu.isma.ismanextpurejfx.javafx.IView
import ru.nstu.isma.ismanextpurejfx.views.settings.SettingsPanelView
import ru.nstu.isma.ismanextpurejfx.views.toolbars.IsmaMenuBar
import ru.nstu.isma.ismanextpurejfx.views.toolbars.IsmaToolBar

class MainView() : IView {
    override val root = BorderPane().apply {
        top = VBox(
            IsmaMenuBar().root,
            IsmaToolBar().root
        )
        center = Text("TornadoFX sucks")
        right = SettingsPanelView().root
    }
}