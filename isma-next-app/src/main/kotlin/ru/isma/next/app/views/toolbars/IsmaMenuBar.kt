package ru.isma.next.app.views.toolbars

import ru.isma.next.app.services.project.ProjectFileService
import ru.isma.next.editor.text.services.contracts.ITextEditorService
import ru.isma.next.app.services.project.ProjectService
import ru.isma.next.app.services.simualtion.SimulationParametersService
import ru.isma.next.app.services.simualtion.SimulationService
import tornadofx.*

class IsmaMenuBar: View() {
    private val projectController: ProjectService by di()
    private val projectFileService: ProjectFileService by di()
    private val simulationService: SimulationService by di()
    private val textEditorService: ITextEditorService by di()
    private val simulationParametersService: SimulationParametersService by di()

    override val root = menubar {
        menu("File") {
            item("New text","Shortcut+N").action {
                projectController.createNew()
            }
            item("New blueprint").action {
                projectController.createNewBlueprint()
            }
            item("Open","Shortcut+O").action {
                projectFileService.open()
            }
            item("Save","Shortcut+S").action {
                projectFileService.save()
            }
            item("Save as...").action {
                projectFileService.saveAs()
            }
            item("Save all").action {
                projectFileService.saveAll()
            }
            separator()
            item("Model settings").action {
                println("Closed!")
            }
            separator()
            item("Close").action {
                println("Closed!")
            }
            item("Close all").action {
                println("Closed all!")
            }
            separator()
            item("Exit","Shortcut+W").action {
                println("Quitting!")
            }
        }
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
        }
    }
}