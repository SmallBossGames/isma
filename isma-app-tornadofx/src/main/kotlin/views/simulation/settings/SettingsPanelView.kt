package views.simulation.settings

import tornadofx.*

class SettingsPanelView : View() {
    private val cauchyInitialsView: CauchyInitialsView by inject()
    private val methodSettingsView: MethodSettingsView by inject()
    private val eventDetectionView: EventDetectionView by inject()
    private val resultProcessingView: ResultProcessingView by inject()

    override val root = drawer {
        multiselect = true
        val itemsWidth = 300.0

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
