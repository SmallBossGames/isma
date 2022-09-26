package ru.nstu.grin.concatenation.description.model

import javafx.collections.FXCollections
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.javafx.JavaFx
import kotlinx.coroutines.launch
import ru.nstu.grin.concatenation.canvas.model.ConcatenationCanvasModel

class DescriptionListViewModel(
    private val concatenationCanvasModel: ConcatenationCanvasModel,
) {
    private val coroutineScope = CoroutineScope(Dispatchers.JavaFx)

    val descriptions = FXCollections.observableArrayList<Description>()!!

    init {
        coroutineScope.launch {
            merge(
                flowOf(concatenationCanvasModel.descriptions),
                concatenationCanvasModel.descriptionsListUpdatedEvent
            ).collect{
                descriptions.setAll(it)
            }
        }
    }
}