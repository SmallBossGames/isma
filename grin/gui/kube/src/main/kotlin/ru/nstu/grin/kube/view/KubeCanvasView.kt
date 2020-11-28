package ru.nstu.grin.kube.view

import javafx.scene.Parent
import tornadofx.FileChooserMode
import tornadofx.View
import tornadofx.action
import tornadofx.chooseFile
import tornadofx.item
import tornadofx.menu
import tornadofx.menubar
import tornadofx.vbox

class KubeCanvasView: View() {
    override val root: Parent = vbox {
        menubar {
            menu("File") {
                item("Save as").action {
                    val file = chooseFile("Файл", arrayOf(), FileChooserMode.Save).first()
//                    fire(SaveEvent(file))
                }
                item("Load").action {
                    val file = chooseFile("Файл", arrayOf(), FileChooserMode.Single).first()
//                    fire(LoadEvent(file))
                }
            }
            menu("Canvas") {
                item("Clear all").action {
                }
            }
        }


    }
}