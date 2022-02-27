package ru.nstu.grin.concatenation.cartesian.service

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.nstu.grin.concatenation.canvas.model.ConcatenationCanvasModel
import ru.nstu.grin.concatenation.canvas.view.ConcatenationCanvas
import ru.nstu.grin.concatenation.cartesian.model.CartesianCopyDataModel
import ru.nstu.grin.concatenation.cartesian.model.CartesianSpace
import ru.nstu.grin.concatenation.cartesian.model.UpdateCartesianDataModel
import tornadofx.Controller
import java.util.*

class CartesianCanvasService : Controller() {
    private val coroutineScope = CoroutineScope(Dispatchers.Default)
    private val model: ConcatenationCanvasModel by inject()
    private val view: ConcatenationCanvas by inject()

    fun copyCartesian(copyDataModel: CartesianCopyDataModel) {
        val oldCartesian = copyDataModel.origin
        val newCartesian = oldCartesian.clone().copy(
            id = UUID.randomUUID(), name = copyDataModel.name,
            xAxis = oldCartesian.xAxis.copy(
                id = UUID.randomUUID(),
                name = copyDataModel.xAxisName,
                order = oldCartesian.xAxis.order + 1,
                settings = oldCartesian.xAxis.settings.copy()
            ),
            yAxis = oldCartesian.yAxis.copy(
                id = UUID.randomUUID(),
                name = copyDataModel.yAxisName,
                order = oldCartesian.yAxis.order + 1,
                settings = oldCartesian.yAxis.settings.copy()
            )
        )
        this.model.cartesianSpaces.add(newCartesian)
        reportUpdateSpaces()
        view.redraw()
    }

    fun updateCartesian(dataModel: UpdateCartesianDataModel) {
        val cartesian = dataModel.space
        cartesian.isShowGrid = dataModel.isShowGrid
        cartesian.name = dataModel.name
        reportUpdateSpaces()
        view.redraw()
    }

    fun deleteCartesianSpace(space: CartesianSpace) {
        model.cartesianSpaces.remove(space)
        refreshDependencies()
        view.redraw()
    }

    private fun refreshDependencies() {
        coroutineScope.launch {
            model.reportUpdateAll()
        }
    }

    private fun reportUpdateSpaces() {
        coroutineScope.launch {
            model.reportCartesianSpacesListUpdate()
        }
    }
}