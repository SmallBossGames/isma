package ru.nstu.grin

import tornadofx.*

/**
 * @author kostya05983
 * MainView contains all components for grin graphic builder
 */
class MainView : View() {

    override val root: javafx.scene.Parent = vbox {
        menubar {
            menu("Stage1") {
                menuitem("Stage1")
            }
            menu("Stage2") {
                menuitem("Stage2")
            }
        }
        add<GrinCanvas>()
    }
}