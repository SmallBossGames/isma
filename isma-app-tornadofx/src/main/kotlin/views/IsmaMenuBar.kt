package views

import controllers.ProjectController
import events.NewProjectEvent
import models.IsmaProjectModel
import tornadofx.*

class IsmaMenuBar : View() {
    val projectController: ProjectController by inject()

    override val root = menubar {
        menu("File") {
            item("New","Shortcut+N").action {
                println("Saving!")
                projectController.createNew("Dick and semen")
            }
            item("Open","Shortcut+O").action {
                println("Open!")
            }
            item("Save","Shortcut+S").action {
                println("Save!")
            }
            item("Save as...","Shortcut+Q").action {
                println("Save!")
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
                println("Copying!")
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