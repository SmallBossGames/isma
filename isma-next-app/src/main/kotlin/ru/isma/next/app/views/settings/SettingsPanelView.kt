package ru.isma.next.app.views.settings

import tornadofx.View
import tornadofx.drawer

class SettingsPanelView(
    private val cauchyInitialsView: CauchyInitialsView,
    private val methodSettingsView: MethodSettingsView,
    private val eventDetectionView: EventDetectionView,
    private val resultProcessingView: ResultProcessingView,
) : View() {
    override val root = drawer {
        multiselect = true
        val itemsWidth = 240.0

        item(cauchyInitialsView.title) {
            prefWidth = itemsWidth
            add(cauchyInitialsView)
        }
        item(methodSettingsView.title) {
            prefWidth = itemsWidth
            add(methodSettingsView)
        }
        item(resultProcessingView.title) {
            prefWidth = itemsWidth
            add(resultProcessingView)
        }
        item(eventDetectionView.title) {
            prefWidth = itemsWidth
            add(eventDetectionView)
        }
    }
}
