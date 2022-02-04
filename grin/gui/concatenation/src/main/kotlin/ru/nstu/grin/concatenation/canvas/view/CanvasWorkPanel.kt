package ru.nstu.grin.concatenation.canvas.view

import javafx.scene.Parent
import javafx.scene.control.TabPane
import ru.nstu.grin.concatenation.canvas.model.ConcatenationCanvasModel
import tornadofx.View
import tornadofx.tab
import tornadofx.tabpane

class CanvasWorkPanel : View() {
    private val concatenationCanvasModel: ConcatenationCanvasModel by inject()
    private val chainDrawer: ConcatenationChainDrawer by inject()

    override val root: Parent = tabpane {
        tabClosingPolicy = TabPane.TabClosingPolicy.UNAVAILABLE

        tab("Chart") {
            content = ChartToolBar(
                concatenationCanvasModel,
                chainDrawer
            )
        }
        tab("Modes") {
            add<ModesPanel>()
        }
        tab("Math") {
            add<MathPanel>()
        }
        tab("Transformations") {
            add<TransformPanel>()
        }
    }
}