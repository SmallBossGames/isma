package ru.nstu.grin.concatenation.function.view

import javafx.scene.layout.VBox
import ru.isma.javafx.extensions.controls.propertiesGrid
import ru.nstu.grin.concatenation.function.model.AnalyticFunctionModel

/**
 * @author Konstantin Volivach
 */
class AnalyticFunctionFragment(
    val model: AnalyticFunctionModel
) : VBox(
    propertiesGrid {
        addNode("Formula", model.textFunctionProperty)
        addNode("Delta", model.deltaProperty)
    }
)