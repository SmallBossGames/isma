package ru.nstu.isma.view

import javafx.scene.Parent
import tornadofx.View
import tornadofx.hbox
import tornadofx.menubar
import tornadofx.vbox

class StateChartView : View() {

    override val root: Parent = vbox {
        menubar {

        }
        hbox {
            add<ElementsView>()
            add<StateChartCanvas>()
        }
    }
}