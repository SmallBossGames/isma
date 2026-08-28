package ru.isma.next.app.views.settings

import javafx.collections.FXCollections
import javafx.scene.control.ScrollPane
import javafx.scene.layout.Pane
import ru.isma.javafx.extensions.controls.propertiesGrid
import ru.isma.next.app.models.simulation.SaveTarget
import ru.isma.next.app.viewmodels.SimulationParametersViewModel

class ResultSavingView(
    private val parametersViewModel: SimulationParametersViewModel
) : Pane() {
    val title: String = "Result saving"

    init {
        val scrollPane = ScrollPane(
            propertiesGrid {
                addNode(
                    "Save result",
                    FXCollections.observableArrayList(SaveTarget.values().toList()),
                    parametersViewModel.resultSaving.savingTargetProperty
                )
            }
        )
        scrollPane.isFitToWidth = true
        children.add(scrollPane)
    }
}
