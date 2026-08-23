package ru.isma.next.app.views.settings

import javafx.scene.control.ScrollPane
import javafx.scene.layout.Pane
import ru.isma.javafx.extensions.controls.propertiesGrid
import ru.isma.next.app.viewmodels.SimulationParametersViewModel

class MethodSettingsView(
    private val parametersViewModel: SimulationParametersViewModel
) : Pane() {
    val title: String = "Integration"

    init {
        val integrationMethod = parametersViewModel.integrationMethod
        val scrollPane = ScrollPane(
            propertiesGrid {
                addNode(
                    "Method",
                    parametersViewModel.integrationMethods,
                    integrationMethod.selectedMethodProperty
                )
                addNode("Accurate", integrationMethod.isAccuracyInUseProperty)
                addNode("Accuracy", integrationMethod.accuracyProperty).apply {
                    disableProperty().bind(integrationMethod.isAccuracyInUseProperty.not())
                }
                addNode("Stable", integrationMethod.isStableInUseProperty)
                addNode("Parallel", integrationMethod.isParallelInUseProperty)
                addNode("Server", integrationMethod.serverProperty).apply {
                    disableProperty().bind(integrationMethod.isParallelInUseProperty.not())
                }
                addNode("Port", integrationMethod.portProperty).apply {
                    disableProperty().bind(integrationMethod.isParallelInUseProperty.not())
                }
            }
        )
        scrollPane.isFitToWidth = true
        children.add(scrollPane)
    }
}
