package ru.nstu.grin.concatenation.function.view

import javafx.scene.layout.VBox
import ru.isma.javafx.extensions.controls.propertiesGrid
import ru.nstu.grin.concatenation.function.model.ManualFunctionModel

class ManualFunctionFragment(
    private val model: ManualFunctionModel
) : VBox(
    propertiesGrid {
        addNode("X Points", model.xPointsProperty)
        addNode("Y Points", model.yPointsProperty)
    }
)