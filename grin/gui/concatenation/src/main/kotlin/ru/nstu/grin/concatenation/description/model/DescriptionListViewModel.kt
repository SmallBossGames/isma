package ru.nstu.grin.concatenation.description.model

import javafx.collections.FXCollections
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.javafx.JavaFx
import kotlinx.coroutines.launch
import ru.nstu.grin.common.model.Description
import ru.nstu.grin.concatenation.canvas.model.ConcatenationCanvasModel
import tornadofx.Scope
import tornadofx.ViewModel

class DescriptionListViewModel(
    override val scope: Scope
) : ViewModel() {
    private val coroutineScope = CoroutineScope(Dispatchers.JavaFx)
    private val concatenationCanvasModel: ConcatenationCanvasModel by inject()

    val descriptions = FXCollections.observableArrayList<Description>()!!

    init {
        coroutineScope.launch {
            merge(
                flowOf(concatenationCanvasModel.getAllDescriptions()),
                concatenationCanvasModel.descriptionsListUpdatedEvent
            ).collect{
                descriptions.setAll(it)
            }
        }
    }
}