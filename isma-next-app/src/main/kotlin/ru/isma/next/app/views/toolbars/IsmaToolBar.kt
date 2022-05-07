package ru.isma.next.app.views.toolbars

import javafx.event.EventHandler
import javafx.scene.control.Button
import javafx.scene.control.Separator
import javafx.scene.control.ToolBar
import javafx.scene.control.Tooltip
import ru.isma.next.app.extentions.matIconAL
import ru.isma.next.app.extentions.matIconMZ
import ru.isma.next.app.services.project.LismaPdeService
import ru.isma.next.app.services.project.ProjectFileService
import ru.isma.next.app.services.project.ProjectService
import ru.isma.next.app.services.simualtion.SimulationParametersService
import ru.isma.next.editor.text.services.contracts.IEditorPlatformService

class IsmaToolBar(
    private val projectController: ProjectService,
    private val projectFileService: ProjectFileService,
    private val lismaPdeService: LismaPdeService,
    private val textEditorService: IEditorPlatformService,
    private val simulationParametersService: SimulationParametersService,
): ToolBar() {
    init {
        items.addAll(
            Button().apply {
                graphic = matIconAL("add_circle_outline")
                tooltip = Tooltip("New model")
                onAction = EventHandler { projectController.createNew() }
            },
            Button().apply {
                graphic = matIconAL("add_box")
                tooltip = Tooltip("New blueprint")
                onAction = EventHandler { projectController.createNewBlueprint() }
            },
            Button().apply {
                graphic = matIconAL("folder_open")
                tooltip = Tooltip("Open model")
                onAction = EventHandler { projectFileService.open() }
            },
            Button().apply {
                graphic = matIconMZ("save")
                tooltip = Tooltip("Save current model")
                onAction = EventHandler { projectFileService.save() }
            },
            Button().apply {
                graphic = matIconMZ("save_alt")
                tooltip = Tooltip("Save all models")
                onAction = EventHandler { projectFileService.saveAll() }
            },
            Separator(),
            Button().apply {
                graphic = matIconAL("content_cut")
                tooltip = Tooltip("Cut")
                onAction = EventHandler { textEditorService.cut() }
            },
            Button().apply {
                graphic = matIconAL("content_copy")
                tooltip = Tooltip("Copy")
                onAction = EventHandler { textEditorService.copy() }
            },
            Button().apply {
                graphic = matIconAL("content_paste")
                tooltip = Tooltip("Paste")
                onAction = EventHandler { textEditorService.paste() }
            },
            Separator(),
            Button().apply {
                graphic = matIconAL("check_circle")
                tooltip = Tooltip("Verify")
                onAction = EventHandler {
                    val lismaSnapshot = projectController.activeProject?.snapshot() ?: return@EventHandler
                    lismaPdeService.translateLisma(lismaSnapshot)
                }
            },
            Separator(),
            Button().apply {
                graphic = matIconAL("bookmark")
                tooltip = Tooltip("Store Settings")
                onAction = EventHandler {
                    simulationParametersService.store()
                }
            },
            Button().apply {
                graphic = matIconAL("bookmark_border")
                tooltip = Tooltip("Load Settings")
                onAction = EventHandler {
                    simulationParametersService.load()
                }
            },
        )
    }
}