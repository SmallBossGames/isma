package ru.isma.next.app.views.toolbars

import javafx.event.EventHandler
import javafx.scene.control.Menu
import javafx.scene.control.MenuBar
import javafx.scene.control.MenuItem
import javafx.scene.control.SeparatorMenuItem
import javafx.scene.input.KeyCombination
import ru.isma.next.app.viewmodels.MainCommandsViewModel

class IsmaMenuBar(
    private val commands: MainCommandsViewModel,
): MenuBar() {
    init {
        menus.addAll(
            Menu("File", null,
                MenuItem("New text", null).apply {
                    accelerator = KeyCombination.keyCombination("Shortcut+N")
                    onAction = EventHandler { commands.newProject() }
                },
                MenuItem("New Statechart").apply {
                    accelerator = KeyCombination.keyCombination("Shortcut+B")
                    onAction = EventHandler { commands.newBlueprint() }
                },
                MenuItem("Open").apply {
                    accelerator = KeyCombination.keyCombination("Shortcut+O")
                    onAction = EventHandler { commands.open(scene?.window) }
                },
                MenuItem("Save").apply {
                    accelerator = KeyCombination.keyCombination("Shortcut+S")
                    onAction = EventHandler { commands.save() }
                },
                MenuItem("Save as...").apply {
                    onAction = EventHandler { commands.saveAs(scene?.window) }
                },
                MenuItem("Save all").apply {
                    onAction = EventHandler { commands.saveAll(scene?.window) }
                },
                SeparatorMenuItem(),
                MenuItem("Close").apply {
                    onAction = EventHandler { commands.closeActive() }
                },
                MenuItem("Close all").apply {
                    onAction = EventHandler { commands.closeAll() }
                },
                SeparatorMenuItem(),
                MenuItem("Exit").apply {
                    accelerator = KeyCombination.keyCombination("Shortcut+W")
                    onAction = EventHandler { commands.exit() }
                }
            ),
            Menu("Edit", null,
                MenuItem("Cut").apply {
                    accelerator = KeyCombination.keyCombination("Shortcut+X")
                    onAction = EventHandler { commands.cut() }
                },
                MenuItem("Copy").apply {
                    accelerator = KeyCombination.keyCombination("Shortcut+C")
                    onAction = EventHandler { commands.copy() }
                },
                MenuItem("Paste").apply {
                    accelerator = KeyCombination.keyCombination("Shortcut+P")
                    onAction = EventHandler { commands.paste() }
                },
            ),
            Menu("Simulation", null,
                MenuItem("Verify").apply {
                    accelerator = KeyCombination.keyCombination("Shortcut+F4")
                    onAction = EventHandler { commands.verify() }
                },
                MenuItem("Run").apply {
                    accelerator = KeyCombination.keyCombination("Shortcut+F5")
                    onAction = EventHandler { commands.simulate() }
                },
                SeparatorMenuItem(),
                MenuItem("Store Settings").apply {
                    onAction = EventHandler { commands.storeSettings(scene?.window) }
                },
                MenuItem("Load Settings").apply {
                    onAction = EventHandler { commands.loadSettings(scene?.window) }
                },
            )
        )
    }
}
