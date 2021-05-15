package views

import controllers.*
import javafx.scene.control.Tooltip
import javafx.scene.image.ImageView
import org.koin.core.component.KoinComponent
import services.FileService
import services.LismaPdeService
import services.ProjectService
import tornadofx.*
import org.koin.core.component.inject as koinInject

class IsmaToolBar : View(), KoinComponent {
    private val projectController: ProjectService by koinInject()
    private val fileService: FileService by koinInject()
    private val lismaPdeService: LismaPdeService by koinInject()

    private val textEditorController: TextEditorController by inject()

    override val root = toolbar {
        button{
            graphic = ImageView("icons/new.png")
            tooltip = Tooltip("New model")
            action { projectController.createNew() }
        }
        button{
            graphic = ImageView("icons/blueprint.png")
            tooltip = Tooltip("New model")
            action { projectController.createNewBlueprint() }
        }
        button{
            graphic = ImageView("icons/open.png")
            tooltip = Tooltip("Open model")
            action { fileService.open() }
        }
        button{
            graphic = ImageView("icons/toolbar/save.png")
            tooltip = Tooltip("Save current model")
            action { fileService.save() }
        }
        button{
            graphic = ImageView("icons/toolbar/saveall.png")
            tooltip = Tooltip("Save all models")
            action { fileService.saveAll() }
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
            action { lismaPdeService.translateLisma() }
        }
        separator()
        button{
            graphic = ImageView("icons/toolbar/settings.png")
            tooltip = Tooltip("ISMA settings")
        }
    }
}