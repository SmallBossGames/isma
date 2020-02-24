package ru.nstu.grin.view.simple

import javafx.scene.Parent
import tornadofx.View
import tornadofx.action
import tornadofx.item
import tornadofx.menu
import tornadofx.menubar
import tornadofx.vbox

class SimpleCanvasView : View() {
    override val root: Parent = vbox {
        menubar {
            menu("File") {
                item("Save as").action {
                }
                item("Load").action {
                }
            }
            menu("Canvas") {
                item("Add function").action {

                }
                item("Clear all").action {
                }
            }
        }
        add<SimpleCanvas>()
    }
}