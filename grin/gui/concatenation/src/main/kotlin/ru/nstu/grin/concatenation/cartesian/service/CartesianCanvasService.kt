package ru.nstu.grin.concatenation.cartesian.service

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.nstu.grin.concatenation.canvas.model.ConcatenationCanvasModel
import ru.nstu.grin.concatenation.cartesian.model.CartesianCopyDataModel
import ru.nstu.grin.concatenation.cartesian.model.CartesianSpace
import ru.nstu.grin.concatenation.cartesian.model.UpdateCartesianDataModel
import java.util.*

class CartesianCanvasService(
    private val model: ConcatenationCanvasModel,
) {
    private val coroutineScope = CoroutineScope(Dispatchers.Default)

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
    }

    fun deleteCartesianSpace(space: CartesianSpace) {
        model.cartesianSpaces.remove(space)
    }

    fun updateCartesian(dataModel: UpdateCartesianDataModel) {
        val cartesian = dataModel.space
        cartesian.isShowGrid = dataModel.isShowGrid
        cartesian.name = dataModel.name

        coroutineScope.launch {
            model.reportCartesianSpacesListUpdate()
        }
    }
}