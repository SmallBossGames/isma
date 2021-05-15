package views

import services.FileService
import services.SimulationController
import controllers.TextEditorController
import org.koin.core.component.KoinComponent
import services.ProjectService
import tornadofx.*
import org.koin.core.component.inject as koinInject

class IsmaMenuBar : View(), KoinComponent {
    private val projectController: ProjectService by koinInject()
    private val fileService: FileService by koinInject()
    private val simulationController: SimulationController by koinInject()
    private val textEditorController: TextEditorController by koinInject()

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
                textEditorController.cut()
            }
            item("Copy","Shortcut+C").action {
                textEditorController.copy()
            }
            item("Paste","Shortcut+V").action {
                textEditorController.paste()
            }
        }
        menu("Simulation") {
            item("Verify","Shortcut+F4").action {
                println("Cut!")
            }
            item("Run","Shortcut+F5").action {
                simulationController.simulate()
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