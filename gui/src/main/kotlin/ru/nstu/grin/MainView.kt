package ru.nstu.grin

import javafx.scene.Parent
import ru.nstu.grin.controller.ClearCanvasEvent
import ru.nstu.grin.controller.LoadEvent
import ru.nstu.grin.controller.SaveEvent
import ru.nstu.grin.view.modal.ChooseFunctionModalView
import ru.nstu.grin.view.GrinCanvas
import tornadofx.*

/**
 * @author kostya05983
 * MainView contains all components for grin graphic builder
 */
class MainView : View() {

    override val root: Parent = vbox {
        menubar {
            menu("Add") {
                item("Function").action {
                    find<ChooseFunctionModalView>().openModal()
                }
            }
            menu("Save") {
                item("Save").action {

                }
                item("Save as").action {
                    val file = chooseFile("Файл", arrayOf(), FileChooserMode.Save).first()

                    fire(SaveEvent(file))
                }
            }
            menu("load") {
                item("load").action {
                    val file = chooseFile("Файл", arrayOf(), FileChooserMode.Single).first()

                    fire(LoadEvent(file))
                }
            }
            menu("Clear") {
                item("Clear all").action {
                    fire(ClearCanvasEvent())
                }
            }
        }
        add<GrinCanvas>()
    }
}