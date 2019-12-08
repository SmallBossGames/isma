package ru.nstu.grin

import javafx.scene.Parent
import ru.nstu.grin.controller.ClearCanvasEvent
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
            menu("Clear") {
                item("Clear all").action {
                    fire(ClearCanvasEvent())
                }
            }
        }
        add<GrinCanvas>()
    }
}