package ru.nstu.grin.concatenation.function.view

import javafx.collections.FXCollections
import javafx.scene.control.Button
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import ru.isma.javafx.extensions.controls.propertiesGrid
import ru.nstu.grin.concatenation.function.controller.DerivativeFunctionController
import ru.nstu.grin.concatenation.function.model.DerivativeTransformerViewModel
import ru.nstu.grin.concatenation.function.transform.DerivativeAxis
import ru.nstu.grin.concatenation.function.transform.DerivativeType

class DerivativeFunctionFragment(
    private val model: DerivativeTransformerViewModel,
    private val controller: DerivativeFunctionController,
) : VBox() {
    init {
        children.addAll(
            propertiesGrid {
                addNode("Degree", model.degreeProperty)
                addNode("Type", FXCollections.observableList(DerivativeType.values().asList()), model.typeProperty)
                addNode("Axis", FXCollections.observableList(DerivativeAxis.values().asList()), model.axisProperty)
            },
            HBox(
                Button("Apply").apply {
                    setOnAction {
                        controller.enableDerivative(model)
                    }
                }
            )
        )
    }
}