package ru.isma.next.app.views.toolbars

import javafx.event.EventHandler
import javafx.scene.control.Menu
import javafx.scene.control.MenuBar
import javafx.scene.control.MenuItem
import javafx.scene.control.SeparatorMenuItem
import javafx.scene.input.KeyCombination
import ru.isma.next.app.services.project.ProjectFileService
import ru.isma.next.app.services.project.ProjectService
import ru.isma.next.app.services.simualtion.SimulationParametersService
import ru.isma.next.app.services.simualtion.SimulationService
import ru.isma.next.editor.text.services.contracts.IEditorPlatformService

class IsmaMenuBar(
    private val projectController: ProjectService,
    private val projectFileService: ProjectFileService,
    private val simulationService: SimulationService,
    private val textEditorService: IEditorPlatformService,
    private val simulationParametersService: SimulationParametersService,
): MenuBar() {
    init {
        menus.addAll(
            Menu("File", null,
                MenuItem("New text", null).apply {
                    accelerator = KeyCombination.keyCombination("Shortcut+N")
                    onAction = EventHandler {
                        projectController.createNew()
                    }
                },
                MenuItem("New Statechart").apply {
                    accelerator = KeyCombination.keyCombination("Shortcut+B")
                    onAction = EventHandler {
                        projectController.createNewBlueprint()
                    }
                },
                MenuItem("Open").apply {
                    accelerator = KeyCombination.keyCombination("Shortcut+O")
                    onAction = EventHandler {
                        projectFileService.open()
                    }
                },
                MenuItem("Save").apply {
                    accelerator = KeyCombination.keyCombination("Shortcut+S")
                    onAction = EventHandler {
                        projectFileService.save()
                    }
                },
                MenuItem("Save as...").apply {
                    onAction = EventHandler {
                        projectFileService.saveAs()
                    }
                },
                MenuItem("Save all").apply {
                    onAction = EventHandler {
                        projectFileService.saveAll()
                    }
                },
                SeparatorMenuItem(),
                MenuItem("Close").apply {
                    onAction = EventHandler {
                        val project = projectController.activeProject
                        if(project != null){
                            projectController.close(project)
                        }
                    }
                },
                MenuItem("Close all").apply {
                    onAction = EventHandler {
                        projectController.closeAll()
                    }
                },
                SeparatorMenuItem(),
                MenuItem("Exit").apply {
                    accelerator = KeyCombination.keyCombination("Shortcut+W")
                    onAction = EventHandler {
                        println("Quitting!")
                    }
                }
            ),
            Menu("Edit", null,
                MenuItem("Cut").apply {
                    accelerator = KeyCombination.keyCombination("Shortcut+X")
                    onAction = EventHandler {
                        textEditorService.cut()
                    }
                },
                MenuItem("Copy").apply {
                    accelerator = KeyCombination.keyCombination("Shortcut+C")
                    onAction = EventHandler {
                        textEditorService.copy()
                    }
                },
                MenuItem("Paste").apply {
                    accelerator = KeyCombination.keyCombination("Shortcut+P")
                    onAction = EventHandler {
                        textEditorService.paste()
                    }
                },
            ),
            Menu("Simulation", null,
                MenuItem("Verify").apply {
                    accelerator = KeyCombination.keyCombination("Shortcut+F4")
                    onAction = EventHandler {
                        println("Verify!")
                    }
                },
                MenuItem("Run").apply {
                    accelerator = KeyCombination.keyCombination("Shortcut+F5")
                    onAction = EventHandler {
                        simulationService.simulate()
                    }
                },
                SeparatorMenuItem(),
                MenuItem("Store Settings").apply {
                    onAction = EventHandler {
                        simulationParametersService.store()
                    }
                },
                MenuItem("Load Settings").apply {
                    onAction = EventHandler {
                        simulationParametersService.load()
                    }
                },
            )
        )
    }
}