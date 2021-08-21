package ru.nstu.isma.ismanextpurejfx.views.toolbars

import javafx.scene.control.Button
import javafx.scene.control.ToolBar
import javafx.scene.image.ImageView
import org.kordamp.ikonli.javafx.FontIcon
import org.kordamp.ikonli.javafx.IkonResolver
import ru.nstu.isma.ismanextpurejfx.javafx.IView

class IsmaToolBar : IView {
    override val root = ToolBar(
        Button("New model").apply {

        }
    )

    /*override val root = toolbar {
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
                val lismaSnapshot = projectController.activeProject?.snapshot() ?: return@action
                lismaPdeService.translateLisma(lismaSnapshot)
            }
        }
        separator()
        button("Load settings").action {
            simulationParametersService.load()
        }
        button("Store settings").action {
            simulationParametersService.store()
        }
    }*/
}