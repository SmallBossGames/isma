package views

import controllers.FileController
import controllers.ProjectController
import controllers.SimulationController
import events.NewProjectEvent
import models.IsmaProjectModel
import tornadofx.*

class IsmaMenuBar : View() {
    private val projectController: ProjectController by inject()
    private val fileController: FileController by inject()
    private val simulationController: SimulationController by inject()

    override val root = menubar {
        menu("File") {
            item("New","Shortcut+N").action {
                projectController.createNew()
            }
            item("Open","Shortcut+O").action {
                fileController.open()
            }
            item("Save","Shortcut+S").action {
                fileController.save()
            }
            item("Save as...").action {
                fileController.saveAs()
            }
            item("Save all").action {
                fileController.saveAll()
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
                println("Cut!")
            }
            item("Copy","Shortcut+C").action {
                println("Copying!")
            }
            item("Paste","Shortcut+V").action {
                println("Pasting!")
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