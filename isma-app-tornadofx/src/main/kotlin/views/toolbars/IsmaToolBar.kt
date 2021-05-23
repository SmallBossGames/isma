package views.toolbars

import javafx.scene.control.Tooltip
import javafx.scene.image.ImageView
import ru.isma.next.common.services.lisma.FailedTranslation
import ru.isma.next.common.services.lisma.services.LismaPdeService
import services.ModelErrorService
import services.project.ProjectFileService
import services.project.ProjectService
import services.editor.TextEditorService
import tornadofx.*

class IsmaToolBar(
    private val projectController: ProjectService,
    private val projectFileService: ProjectFileService,
    private val lismaPdeService: LismaPdeService,
    private val textEditorService: TextEditorService,
    private val modelService: ModelErrorService,
) : View() {
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
            action { projectFileService.open() }
        }
        button{
            graphic = ImageView("icons/toolbar/save.png")
            tooltip = Tooltip("Save current model")
            action { projectFileService.save() }
        }
        button{
            graphic = ImageView("icons/toolbar/saveall.png")
            tooltip = Tooltip("Save all models")
            action { projectFileService.saveAll() }
        }
        separator()
        button{
            graphic = ImageView("icons/toolbar/cut.png")
            tooltip = Tooltip("Cut")
            action { textEditorService.cut() }
        }
        button{
            graphic = ImageView("icons/toolbar/copy.png")
            tooltip = Tooltip("Copy")
            action { textEditorService.copy() }
        }
        button{
            graphic = ImageView("icons/toolbar/paste.png")
            tooltip = Tooltip("Paste")
            action { textEditorService.paste() }
        }
        separator()
        button{
            graphic = ImageView("icons/toolbar/checked.png")
            tooltip = Tooltip("Verify")
            action {
                val text = projectController.activeProject?.lismaText?:return@action
                val translationResult = lismaPdeService.translateLisma(text)

                modelService.setErrorList(emptyList())

                if(translationResult is FailedTranslation) {
                    modelService.setErrorList(translationResult.errors)
                }
            }
        }
        separator()
        button{
            graphic = ImageView("icons/toolbar/settings.png")
            tooltip = Tooltip("ISMA settings")
        }
    }
}