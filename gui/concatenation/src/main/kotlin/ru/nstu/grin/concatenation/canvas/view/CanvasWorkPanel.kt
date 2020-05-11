package ru.nstu.grin.concatenation.canvas.view

import javafx.scene.Parent
import tornadofx.View
import tornadofx.tab
import tornadofx.tabpane

class CanvasWorkPanel : View() {
    override val root: Parent = tabpane {
        tab("Режимы") {
            add<ModesPanel>()
        }
        tab("Математические операции") {
            add<MathPanel>()
        }
        tab("Графические преобразования") {
            add<TransformPanel>()
        }
    }
}