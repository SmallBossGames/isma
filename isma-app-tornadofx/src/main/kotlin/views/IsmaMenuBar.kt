package views

import services.FileService
import services.SimulationService
import services.TextEditorService
import services.ProjectService
import tornadofx.*

class IsmaMenuBar(
    private val projectController: ProjectService,
    private val fileService: FileService,
    private val simulationService: SimulationService,
    private val textEditorService: TextEditorService
) : View() {

    override val root = menubar {
        menu("File") {
            item("New text","Shortcut+N").action {
                projectController.createNew()
            }
            item("New blueprint").action {
                projectController.createNewBlueprint()
            }
            item("Open","Shortcut+O").action {
                fileService.open()
            }
            item("Save","Shortcut+S").action {
                fileService.save()
            }
            item("Save as...").action {
                fileService.saveAs()
            }
            item("Save all").action {
                fileService.saveAll()
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