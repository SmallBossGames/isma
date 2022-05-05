package ru.nstu.grin.concatenation.function.view

import javafx.collections.FXCollections
import javafx.geometry.Insets
import javafx.scene.control.Button
import javafx.scene.layout.BorderPane
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import ru.isma.javafx.extensions.controls.propertiesGrid
import ru.nstu.grin.concatenation.function.controller.FunctionIntegrationController
import ru.nstu.grin.concatenation.function.model.FunctionIntegrationViewModel
import ru.nstu.grin.concatenation.function.transform.IntegrationAxis
import ru.nstu.grin.concatenation.function.transform.IntegrationMethod

class FunctionIntegrationView(
    viewModel: FunctionIntegrationViewModel,
    controller: FunctionIntegrationController
): BorderPane() {
    val title = "Find Integral"

    init {
        val transformerViewModel = viewModel.transformerViewModel
        center = VBox(
            propertiesGrid {
                addNode("Initial", transformerViewModel.initialValueProperty)
                addNode("Method", FXCollections.observableList(IntegrationMethod.values().asList()), transformerViewModel.methodProperty)
                addNode("Axis", FXCollections.observableList(IntegrationAxis.values().asList()), transformerViewModel.axisProperty)
            }
        ).apply {
            padding = Insets(10.0)
        }

        bottom = HBox(
            Button("Find").apply {
                setOnAction {
                    controller.findIntegral(viewModel)
                }
            }
        ).apply {
            padding = Insets(10.0)
        }
    }
}