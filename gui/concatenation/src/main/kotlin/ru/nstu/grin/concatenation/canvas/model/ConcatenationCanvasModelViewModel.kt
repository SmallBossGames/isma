package ru.nstu.grin.concatenation.canvas.model

import javafx.beans.property.SimpleListProperty
import javafx.collections.FXCollections
import ru.nstu.grin.common.model.Arrow
import ru.nstu.grin.common.model.Description
import ru.nstu.grin.concatenation.canvas.view.ConcatenationCanvas
import tornadofx.ItemViewModel
import tornadofx.*

class ConcatenationCanvasModelViewModel : ItemViewModel<ConcatenationCanvas>() {
    var cartesianSpaceProperty = SimpleListProperty<CartesianSpace>(FXCollections.observableArrayList())
    var cartesianSpaces by cartesianSpaceProperty

    var arrowsProperty = SimpleListProperty<Arrow>(FXCollections.observableArrayList())
    var arrows by arrowsProperty

    var descriptionsProperty = SimpleListProperty<Description>(FXCollections.observableArrayList())
    var descriptions by descriptionsProperty

    val pointToolTipSettings =
        PointToolTipsSettings(false, mutableSetOf())

    val contextMenuSettings =
        ContextMenuSettings(ContextMenuType.NONE, 0.0, 0.0)
}