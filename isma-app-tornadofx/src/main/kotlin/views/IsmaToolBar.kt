package views

import controllers.*
import javafx.scene.control.Tooltip
import javafx.scene.image.ImageView
import javafx.stage.Modality
import tornadofx.*

class IsmaToolBar : View() {
    private val lismaPdeController: LismaPdeController by inject()
    private val projectController: ProjectController by inject()
    private val fileController: FileController by inject()
    private val textEditorController: TextEditorController by inject()

    override val root = toolbar {
        button{
            graphic = ImageView("icons/new.png")
            tooltip = Tooltip("New model")
            action { projectController.createNew() }
        }
        button{
            graphic = ImageView("icons/open.png")
            tooltip = Tooltip("Open model")
            action { fileController.open() }
        }
        button{
            graphic = ImageView("icons/toolbar/save.png")
            tooltip = Tooltip("Save current model")
            action { fileController.save() }
        }
        button{
            graphic = ImageView("icons/toolbar/saveall.png")
            tooltip = Tooltip("Save all models")
            action { fileController.saveAll() }
        }
        separator()
        button{
            graphic = ImageView("icons/toolbar/cut.png")
            tooltip = Tooltip("Cut")
            action { textEditorController.cut() }
        }
        button{
            graphic = ImageView("icons/toolbar/copy.png")
            tooltip = Tooltip("Copy")
            action { textEditorController.copy() }
        }
        button{
            graphic = ImageView("icons/toolbar/paste.png")
            tooltip = Tooltip("Paste")
            action { textEditorController.paste() }
        }
        separator()
        button{
            graphic = ImageView("icons/toolbar/checked.png")
            tooltip = Tooltip("Verify")
            action { lismaPdeController.translateLisma() }
        }
        separator()
        button{
            graphic = ImageView("icons/toolbar/settings.png")
            tooltip = Tooltip("ISMA settings")
        }
    }
}