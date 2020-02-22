package ru.nstu.grin.view.simple

import javafx.scene.Parent
import javafx.scene.canvas.Canvas
import ru.nstu.grin.settings.SettingsProvider
import tornadofx.View
import tornadofx.action
import tornadofx.canvas
import tornadofx.item
import tornadofx.menu
import tornadofx.menubar
import tornadofx.vbox

class SimpleCanvasView : View() {
    private lateinit var chainDrawer: SimpleChainDrawer

    override val root: Parent = vbox {
        menubar {
            menu("File") {
                item("Save as").action {
                }
                item("Load").action {
                }
            }
            menu("Canvas") {
                item("Clear all").action {
                }
            }
        }
        canvas(SettingsProvider.getCanvasWidth(), SettingsProvider.getCanvasHeight()) {
            chainDrawer = SimpleChainDrawer(this)
            chainDrawer.draw()
        }
    }
}