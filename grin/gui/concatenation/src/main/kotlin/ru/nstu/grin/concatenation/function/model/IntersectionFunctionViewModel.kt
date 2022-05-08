package ru.nstu.grin.concatenation.function.model

import javafx.beans.property.SimpleDoubleProperty
import javafx.beans.property.SimpleListProperty
import javafx.collections.FXCollections
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.javafx.JavaFx
import kotlinx.coroutines.launch
import ru.isma.javafx.extensions.helpers.getValue
import ru.isma.javafx.extensions.helpers.setValue
import ru.nstu.grin.concatenation.canvas.model.ConcatenationCanvasModel
import ru.nstu.grin.concatenation.function.service.FunctionOperationsService

class IntersectionFunctionViewModel(
    concatenationCanvasModel: ConcatenationCanvasModel,
    private val functionCanvasService: FunctionOperationsService,
) {
    private val fxCoroutineScope = CoroutineScope(Dispatchers.JavaFx)

    val functions = FXCollections.observableArrayList<ConcatenationFunction>()!!
    val selectedFunctions = SimpleListProperty<ConcatenationFunction>()

    val mergeIntervalsDistanceProperty = SimpleDoubleProperty()
    var mergeIntervalsDistance by mergeIntervalsDistanceProperty

    init {
        fxCoroutineScope.launch {
            merge(
                flowOf(concatenationCanvasModel.functions),
                concatenationCanvasModel.functionsListUpdatedEvent
            ).collect{
                functions.setAll(it)
            }
        }

        mergeIntervalsDistance = 0.0
    }

    suspend fun commit() {
        functionCanvasService.showInterSections(
            selectedFunctions[0],
            selectedFunctions[1],
            mergeIntervalsDistance
        )
    }

    fun dispose() {
        fxCoroutineScope.cancel()
    }
}