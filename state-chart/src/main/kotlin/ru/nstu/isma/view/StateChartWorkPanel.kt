package ru.nstu.isma.view

import javafx.scene.Parent
import javafx.scene.layout.Priority
import tornadofx.*

class StateChartWorkPanel : View() {
    override val root: Parent = stackpane {
        canvas(1024.0, 800.0) {
            vgrow = Priority.ALWAYS
            hgrow = Priority.ALWAYS

        }
    }

    fun redraw() {

    }
}