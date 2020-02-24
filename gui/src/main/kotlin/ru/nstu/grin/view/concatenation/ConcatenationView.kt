package ru.nstu.grin.view.concatenation

import javafx.scene.Parent
import ru.nstu.grin.events.concatenation.ClearCanvasEvent
import ru.nstu.grin.events.concatenation.LoadEvent
import ru.nstu.grin.events.concatenation.SaveEvent
import tornadofx.*

class ConcatenationView : View() {
    override val root: Parent = vbox {
        menubar {
            menu("File") {
                item("Save as").action {
                    val file = chooseFile("Файл", arrayOf(), FileChooserMode.Save).first()
                    fire(SaveEvent(file))
                }
                item("Load").action {
                    val file = chooseFile("Файл", arrayOf(), FileChooserMode.Single).first()
                    fire(LoadEvent(file))
                }
            }
            menu("Canvas") {
                item("Clear all").action {
                    fire(ClearCanvasEvent())
                }
            }
        }
        add<ConcatenationCanvas>()
    }
}