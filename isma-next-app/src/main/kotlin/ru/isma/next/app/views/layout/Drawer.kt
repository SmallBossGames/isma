package ru.isma.next.app.views.layout

import javafx.geometry.Orientation
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.ToggleButton
import javafx.scene.control.ToolBar
import javafx.scene.layout.BorderPane
import javafx.scene.layout.Pane

class Drawer : BorderPane() {
    init {
        left = ToolBar().apply {
            orientation = Orientation.VERTICAL
            val pane = Pane()
            val button = ToggleButton("Parameters").apply {
                orientation = Orientation.VERTICAL
                rotate = -90.0
            }
            items.addAll(
                pane,
                button
            )
        }
        center = Pane().apply {
            prefWidth = 200.0
        }
    }
}