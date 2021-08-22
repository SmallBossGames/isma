package ru.isma.next.app.views.toolbars

import javafx.event.EventHandler
import javafx.scene.control.Button
import javafx.scene.control.Separator
import javafx.scene.control.ToolBar
import javafx.scene.control.Tooltip
import javafx.scene.image.ImageView
import ru.isma.next.app.services.project.LismaPdeService
import ru.isma.next.app.services.project.ProjectFileService
import ru.isma.next.app.services.project.ProjectService
import ru.isma.next.app.services.simualtion.SimulationParametersService
import ru.isma.next.editor.text.services.contracts.ITextEditorService

class IsmaToolBar(
    private val projectController: ProjectService,
    private val projectFileService: ProjectFileService,
    private val lismaPdeService: LismaPdeService,
    private val textEditorService: ITextEditorService,
    private val simulationParametersService: SimulationParametersService,
): ToolBar() {
    init {
        items.addAll(
            Button().apply {
                graphic = ImageView("icons/new.png")
                tooltip = Tooltip("New model")
                onAction = EventHandler { projectController.createNew() }
            },
            Button().apply {
                graphic = ImageView("icons/blueprint.png")
                tooltip = Tooltip("New blueprint")
                onAction = EventHandler { projectController.createNewBlueprint() }
            },
            Button().apply {
                graphic = ImageView("icons/open.png")
                tooltip = Tooltip("Open model")
                onAction = EventHandler { projectFileService.open() }
            },
            Button().apply {
                graphic = ImageView("icons/toolbar/save.png")
                tooltip = Tooltip("Save current model")
                onAction = EventHandler { projectFileService.save() }
            },
            Button().apply {
                graphic = ImageView("icons/toolbar/saveall.png")
                tooltip = Tooltip("Save all models")
                onAction = EventHandler { projectFileService.saveAll() }
            },
            Separator(),
            Button().apply {
                graphic = ImageView("icons/toolbar/cut.png")
                tooltip = Tooltip("Cut")
                onAction = EventHandler { textEditorService.cut() }
            },
            Button().apply {
                graphic = ImageView("icons/toolbar/copy.png")
                tooltip = Tooltip("Copy")
                onAction = EventHandler { textEditorService.copy() }
            },
            Button().apply {
                graphic = ImageView("icons/toolbar/paste.png")
                tooltip = Tooltip("Paste")
                onAction = EventHandler { textEditorService.paste() }
            },
            Separator(),
            Button().apply {
                graphic = ImageView("icons/toolbar/checked.png")
                tooltip = Tooltip("Verify")
                onAction = EventHandler {
                    val lismaSnapshot = projectController.activeProject?.snapshot() ?: return@EventHandler
                    lismaPdeService.translateLisma(lismaSnapshot)
                }
            },
            Separator(),
            Button("Load Settings").apply {
                onAction = EventHandler {
                    simulationParametersService.load()
                }
            },
            Button("Store Settings").apply {
                onAction = EventHandler {
                    simulationParametersService.store()
                }
            }
        )
    }
}