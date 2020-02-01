package ru.nstu.grin

import javafx.scene.Parent
import ru.nstu.grin.controller.events.ClearCanvasEvent
import ru.nstu.grin.controller.events.LoadEvent
import ru.nstu.grin.controller.events.SaveEvent
import ru.nstu.grin.view.GrinCanvas
import tornadofx.*

/**
 * @author kostya05983
 * MainView contains all components for grin graphic builder
 */
class MainView : View() {

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
        add<GrinCanvas>()
    }
}