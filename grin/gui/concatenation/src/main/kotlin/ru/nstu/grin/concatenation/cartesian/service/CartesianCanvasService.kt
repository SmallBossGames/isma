package ru.nstu.grin.concatenation.cartesian.service

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.nstu.grin.concatenation.canvas.model.ConcatenationCanvasModel
import ru.nstu.grin.concatenation.cartesian.model.CartesianCopyDataModel
import ru.nstu.grin.concatenation.cartesian.model.CartesianSpace
import ru.nstu.grin.concatenation.cartesian.model.UpdateCartesianDataModel

class CartesianCanvasService(
    private val model: ConcatenationCanvasModel,
) {
    private val coroutineScope = CoroutineScope(Dispatchers.Default)

    fun copyCartesian(copyDataModel: CartesianCopyDataModel) {
        val oldCartesian = copyDataModel.origin
        val newCartesian = oldCartesian.clone().copy(
            name = copyDataModel.name,
            xAxis = oldCartesian.xAxis.copy(
                name = copyDataModel.xAxisName,
            ),
            yAxis = oldCartesian.yAxis.copy(
                name = copyDataModel.yAxisName,
            )
        )

        this.model.cartesianSpaces.add(newCartesian)

        reportSpaceUpdate()
    }

    fun deleteCartesianSpace(space: CartesianSpace) {
        model.cartesianSpaces.remove(space)

        reportSpaceUpdate()
    }

    fun updateCartesian(dataModel: UpdateCartesianDataModel) {
        val cartesian = dataModel.space
        cartesian.isShowGrid = dataModel.isShowGrid
        cartesian.name = dataModel.name

        reportSpaceUpdate()
    }

    private fun reportSpaceUpdate() {
        coroutineScope.launch {
            model.reportCartesianSpacesListUpdate()
        }
    }
}