package ru.nstu.grin.concatenation.canvas.model

import javafx.beans.property.SimpleListProperty
import javafx.collections.FXCollections
import ru.nstu.grin.common.model.Arrow
import ru.nstu.grin.common.model.Description
import ru.nstu.grin.concatenation.canvas.view.ConcatenationCanvas
import ru.nstu.grin.concatenation.cartesian.model.CartesianSpace
import ru.nstu.grin.concatenation.function.model.ConcatenationFunction
import ru.nstu.grin.concatenation.points.model.PointToolTipsSettings
import tornadofx.ItemViewModel
import tornadofx.*

class ConcatenationCanvasModel : ItemViewModel<ConcatenationCanvas>(), Cloneable {
    var cartesianSpaceProperty = SimpleListProperty<CartesianSpace>(FXCollections.observableArrayList())
    var cartesianSpaces by cartesianSpaceProperty

    var arrowsProperty = SimpleListProperty<Arrow>(FXCollections.observableArrayList())
    var arrows by arrowsProperty

    var descriptionsProperty = SimpleListProperty<Description>(FXCollections.observableArrayList())
    var descriptions by descriptionsProperty

    val pointToolTipSettings = PointToolTipsSettings(false, mutableSetOf())

    val contextMenuSettings =
        ContextMenuSettings(ContextMenuType.NONE, 0.0, 0.0)

    val selectionSettings = SelectionSettings()

    var traceSettings: TraceSettings? = null

    var moveSettings: MoveSettings? = null

    fun unselectAll() {
        for (cartesianSpace in cartesianSpaces) {
            for (function in cartesianSpace.functions) {
                function.isSelected = false
            }
        }
        for (description in descriptions) {
            description.isSelected = false
        }
    }

    fun getSelectedFunction(): ConcatenationFunction? {
        return cartesianSpaces.map { it.functions }.flatten().firstOrNull { it.isSelected }
    }

    fun getSelectedDescription(): Description? {
        return descriptions.firstOrNull { it.isSelected }
    }
}