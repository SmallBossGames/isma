package ru.nstu.grin.concatenation.axis.view

import javafx.geometry.Insets
import javafx.scene.layout.VBox
import ru.isma.javafx.extensions.controls.PropertiesGrid
import ru.nstu.grin.concatenation.axis.model.LogarithmicFragmentModel

class LogarithmicTypeFragment(
    model: LogarithmicFragmentModel,
) : VBox(
    PropertiesGrid().apply {
        addNode("Logarithm Base", model.logarithmBaseProperty)
        addNode("Only Integer Pow", model.onlyIntegerPowProperty)
        addNode("Integer Step", model.onlyIntegerPowProperty)
    }
) {
    init {
        padding = Insets(10.0)
    }
}