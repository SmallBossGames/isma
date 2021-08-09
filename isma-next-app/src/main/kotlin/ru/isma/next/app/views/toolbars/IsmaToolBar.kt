package ru.isma.next.app.views.toolbars

import javafx.scene.control.Tooltip
import javafx.scene.image.ImageView
import ru.isma.next.common.services.lisma.FailedTranslation
import ru.isma.next.common.services.lisma.services.LismaPdeService
import ru.isma.next.app.services.ModelErrorService
import ru.isma.next.app.services.project.ProjectFileService
import ru.isma.next.app.services.project.ProjectService
import ru.isma.next.app.services.simualtion.SimulationParametersService
import ru.isma.next.common.services.lisma.models.ErrorViewModel
import ru.isma.next.editor.text.services.contracts.ITextEditorService
import tornadofx.*

class IsmaToolBar: View() {
    private val projectController: ProjectService by di()
    private val projectFileService: ProjectFileService by di()
    private val lismaPdeService: LismaPdeService by di()
    private val textEditorService: ITextEditorService by di()
    private val modelService: ModelErrorService by di()
    private val simulationParametersService: SimulationParametersService by di()

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

                modelService.putErrorList(emptyList())

                if(translationResult is FailedTranslation) {
                    modelService.putErrorList(translationResult.errors.map { ErrorViewModel.fromIsmaErrorModel(it) })
                }
            }
        }
        separator()
        button("Load settings").action {
            simulationParametersService.load()
        }
        button("Store settings").action {
            simulationParametersService.store()
        }
    }
}