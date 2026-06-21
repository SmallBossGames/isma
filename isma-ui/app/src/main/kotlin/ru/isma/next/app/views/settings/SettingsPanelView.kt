package ru.isma.next.app.views.settings

import javafx.scene.layout.VBox
import ru.isma.javafx.extensions.controls.PropertiesAccordion
import ru.isma.javafx.extensions.controls.propertiesGrid

class SettingsPanelView(
    private val cauchyInitialsView: CauchyInitialsView,
    private val methodSettingsView: MethodSettingsView,
    private val eventDetectionView: EventDetectionView,
    private val resultProcessingView: ResultProcessingView,
) : PropertiesAccordion() {
    init {
        addItem(cauchyInitialsView.title, settingsBox(cauchyInitialsView))
        addItem(methodSettingsView.title, settingsBox(methodSettingsView))
        addItem(eventDetectionView.title, settingsBox(eventDetectionView))
        addItem(resultProcessingView.title, settingsBox(resultProcessingView))

        // Expand all by default
        accordion.openPanes.clear()
        accordion.openPanes.addAll(
            accordions.map { it.second.lookup(".titled-pane") as javafx.scene.control.TitledPane }
        )
    }

    private fun settingsBox(view: javafx.scene.Node): VBox {
        return VBox(view).apply {
            prefWidth = 240.0
            styleClass.add("settings-box")
        }
    }
}
