package ru.isma.next.app.views.layout

import javafx.geometry.Orientation
import javafx.scene.control.Button
import javafx.scene.control.Label
import javafx.scene.control.ToolBar
import javafx.scene.layout.BorderPane
import javafx.scene.layout.Pane

class Drawer : BorderPane() {
    init {
        left = ToolBar().apply {
            orientation = Orientation.VERTICAL
            items.addAll(

                Button("Parameters").apply {
                    orientation = Orientation.VERTICAL
                }
            )
        }
        center = Pane().apply {
            prefWidth = 200.0
        }
    }
}