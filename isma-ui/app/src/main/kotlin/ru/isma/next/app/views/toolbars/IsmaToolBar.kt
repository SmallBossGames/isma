package ru.isma.next.app.views.toolbars

import javafx.event.EventHandler
import javafx.scene.control.Button
import javafx.scene.control.Separator
import javafx.scene.control.ToolBar
import javafx.scene.control.Tooltip
import ru.isma.next.app.extensions.matIconAL
import ru.isma.next.app.extensions.matIconMZ
import ru.isma.next.app.viewmodels.MainCommandsViewModel

class IsmaToolBar(
    private val commands: MainCommandsViewModel,
): ToolBar() {
    init {
        items.addAll(
            Button().apply {
                graphic = matIconAL("add_circle_outline")
                tooltip = Tooltip("New model")
                onAction = EventHandler { commands.newProject() }
            },
            Button().apply {
                graphic = matIconAL("add_box")
                tooltip = Tooltip("New statechart")
                onAction = EventHandler { commands.newBlueprint() }
            },
            Button().apply {
                graphic = matIconAL("folder_open")
                tooltip = Tooltip("Open model")
                onAction = EventHandler { commands.open(scene?.window) }
            },
            Button().apply {
                graphic = matIconMZ("save")
                tooltip = Tooltip("Save current model")
                onAction = EventHandler { commands.save() }
            },
            Button().apply {
                graphic = matIconMZ("save_alt")
                tooltip = Tooltip("Save all models")
                onAction = EventHandler { commands.saveAll(scene?.window) }
            },
            Separator(),
            Button().apply {
                graphic = matIconAL("content_cut")
                tooltip = Tooltip("Cut")
                onAction = EventHandler { commands.cut() }
            },
            Button().apply {
                graphic = matIconAL("content_copy")
                tooltip = Tooltip("Copy")
                onAction = EventHandler { commands.copy() }
            },
            Button().apply {
                graphic = matIconAL("content_paste")
                tooltip = Tooltip("Paste")
                onAction = EventHandler { commands.paste() }
            },
            Separator(),
            Button().apply {
                graphic = matIconAL("check_circle")
                tooltip = Tooltip("Verify")
                onAction = EventHandler { commands.verify() }
            },
            Separator(),
            Button().apply {
                graphic = matIconAL("bookmark")
                tooltip = Tooltip("Store Settings")
                onAction = EventHandler { commands.storeSettings(scene?.window) }
            },
            Button().apply {
                graphic = matIconAL("bookmark_border")
                tooltip = Tooltip("Load Settings")
                onAction = EventHandler { commands.loadSettings(scene?.window) }
            },
        )
    }
}
