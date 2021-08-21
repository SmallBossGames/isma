package ru.nstu.isma.ismanextpurejfx.views.toolbars

import javafx.scene.control.*
import javafx.scene.input.KeyCombination
import ru.nstu.isma.ismanextpurejfx.javafx.IView

class IsmaMenuBar : IView {
    override val root = MenuBar(
        Menu(
            "File", null,
            MenuItem("New text").apply {
                accelerator = KeyCombination.keyCombination("Shortcut+N")
            },
            MenuItem("New blueprint").apply {
                //projectController.createNewBlueprint()
            },
            MenuItem("Open").apply {
                //projectFileService.open()
            },
            MenuItem("Save").apply {

            },
            MenuItem("Save as...").apply {

            },
            MenuItem("Save all").apply {

            },
            SeparatorMenuItem(),
            MenuItem("Model settings").apply {

            },
            SeparatorMenuItem(),
            MenuItem("Close").apply {

            },
            MenuItem("Close all").apply {

            },
            SeparatorMenuItem(),
            MenuItem("Exit").apply {

            }
        ),
        Menu(
            "Edit", null,
            MenuItem("Cut").apply {
                accelerator = KeyCombination.keyCombination("Shortcut+X")
            },
            MenuItem("Copy").apply {
                accelerator = KeyCombination.keyCombination("Shortcut+C")
                //projectController.createNewBlueprint()
            },
            MenuItem("Paste").apply {
                accelerator = KeyCombination.keyCombination("Shortcut+V")
                //projectFileService.open()
            },
        ),
        Menu(
            "Simulation", null,
            MenuItem("Verify").apply {
                accelerator = KeyCombination.keyCombination("Shortcut+X")
            },
            MenuItem("Run").apply {
                accelerator = KeyCombination.keyCombination("Shortcut+C")
                //projectController.createNewBlueprint()
            },
            SeparatorMenuItem(),
            MenuItem("Store Settings").apply {
                accelerator = KeyCombination.keyCombination("Shortcut+V")
                //projectFileService.open()
            },
            MenuItem("Load Settings").apply {
                accelerator = KeyCombination.keyCombination("Shortcut+V")
                //projectFileService.open()
            },
        )
    )
}

        /*
        menu("Edit") {
            item("Cut","Shortcut+X").action {
                textEditorService.cut()
            }
            item("Copy","Shortcut+C").action {
                textEditorService.copy()
            }
            item("Paste","Shortcut+V").action {
                textEditorService.paste()
            }
        }
        menu("Simulation") {
            item("Verify","Shortcut+F4").action {
                println("Cut!")
            }
            item("Run","Shortcut+F5").action {
                simulationService.simulate()
            }
            separator()
            item("Store Settings").action {
                simulationParametersService.store()
            }
            item("Load Settings").action {
                simulationParametersService.load()
            }
        }
        menu("Settings") {
            item("ISMA settings","Shortcut+P").action {
                println("Cut!")
            }
            item("Language settings","Shortcut+L").action {
                println("Cut!")
            }
        }*/
