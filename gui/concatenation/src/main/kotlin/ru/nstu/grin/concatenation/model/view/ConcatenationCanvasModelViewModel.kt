package ru.nstu.grin.concatenation.model.view

import javafx.beans.property.SimpleListProperty
import javafx.collections.FXCollections
import ru.nstu.grin.common.model.Arrow
import ru.nstu.grin.concatenation.model.CartesianSpace
import ru.nstu.grin.common.model.Description
import ru.nstu.grin.concatenation.model.ContextMenuSettings
import ru.nstu.grin.concatenation.model.ContextMenuType
import ru.nstu.grin.concatenation.model.PointToolTipSettings
import ru.nstu.grin.concatenation.view.ConcatenationCanvas
import tornadofx.ItemViewModel
import tornadofx.*

class ConcatenationCanvasModelViewModel : ItemViewModel<ConcatenationCanvas>() {
    var cartesianSpaceProperty = SimpleListProperty<CartesianSpace>(FXCollections.observableArrayList())
    var cartesianSpaces by cartesianSpaceProperty

    var arrowsProperty = SimpleListProperty<Arrow>(FXCollections.observableArrayList())
    var arrows by arrowsProperty

    var descriptionsProperty = SimpleListProperty<Description>(FXCollections.observableArrayList())
    var descriptions by descriptionsProperty

    val pointToolTipSettings = PointToolTipSettings(false, 0.0, 0.0, 0.0, 0.0)

    val contextMenuSettings = ContextMenuSettings(ContextMenuType.NONE, 0.0, 0.0)
}