package views

import controllers.LismaPdeController
import controllers.ProjectController
import javafx.scene.control.Tooltip
import javafx.scene.image.ImageView
import javafx.stage.Modality
import tornadofx.*

class IsmaToolBar : View() {
    val lismaPdeController: LismaPdeController by inject()
    val projectController: ProjectController by inject()

    val simulationSettingView: SimulationSettingsView by inject()

    override val root = toolbar {
        button{
            graphic = ImageView("icons/new.png")
            tooltip = Tooltip("New model")
            action { projectController.createNew() }
        }
        button{
            graphic = ImageView("icons/open.png")
            tooltip = Tooltip("Open model")
        }
        button{
            graphic = ImageView("icons/toolbar/save.png")
            tooltip = Tooltip("Save current model")
        }
        button{
            graphic = ImageView("icons/toolbar/saveall.png")
            tooltip = Tooltip("Save all models")
        }
        separator()
        button{
            graphic = ImageView("icons/toolbar/cut.png")
            tooltip = Tooltip("Cut")
        }
        button{
            graphic = ImageView("icons/toolbar/copy.png")
            tooltip = Tooltip("Copy")
        }
        button{
            graphic = ImageView("icons/toolbar/paste.png")
            tooltip = Tooltip("Paste")
        }
        separator()
        button{
            graphic = ImageView("icons/toolbar/checked.png")
            tooltip = Tooltip("Verify")
            action {
                lismaPdeController.translateLisma()
            }
        }
        button{
            graphic = ImageView("icons/toolbar/play.png")
            tooltip = Tooltip("Play")
            action {
                simulationSettingView.openWindow(
                        owner = currentWindow,
                        modality = Modality.WINDOW_MODAL)
            }
        }
        separator()
        button{
            graphic = ImageView("icons/toolbar/settings.png")
            tooltip = Tooltip("ISMA settings")
        }
    }
}