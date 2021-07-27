package views.toolbars

import services.project.ProjectFileService
import ru.isma.next.editor.text.services.TextEditorService
import ru.isma.next.editor.text.services.contracts.ITextEditorService
import services.project.ProjectService
import services.simualtion.SimulationService
import tornadofx.*

class IsmaMenuBar(
    private val projectController: ProjectService,
    private val projectFileService: ProjectFileService,
    private val simulationService: SimulationService,
    private val textEditorService: ITextEditorService
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