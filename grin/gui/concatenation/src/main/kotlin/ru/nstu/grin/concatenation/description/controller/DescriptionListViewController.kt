package ru.nstu.grin.concatenation.description.controller

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.javafx.JavaFx
import kotlinx.coroutines.launch
import ru.nstu.grin.concatenation.canvas.model.ConcatenationCanvasModel
import ru.nstu.grin.concatenation.description.events.DeleteDescriptionQuery
import ru.nstu.grin.concatenation.description.model.DescriptionListViewModel
import ru.nstu.grin.concatenation.description.view.ChangeDescriptionFragment
import tornadofx.Controller
import java.util.*

class DescriptionListViewController : Controller() {
    private val coroutineScope = CoroutineScope(Dispatchers.JavaFx)
    private val concatenationCanvasModel: ConcatenationCanvasModel by inject()
    private val model: DescriptionListViewModel by inject()

    init {
        coroutineScope.launch {
            concatenationCanvasModel.descriptionsListUpdatedEvent.collect{
                model.descriptions.setAll(it)
            }
        }

        model.descriptions.setAll(concatenationCanvasModel.getAllDescriptions())
    }

    fun openChangeModal(id: UUID) {
        find<ChangeDescriptionFragment>(
            mapOf(
                ChangeDescriptionFragment::descriptionId to id
            )
        ).openModal()
    }

    fun deleteDescription(id: UUID) {
        fire(DeleteDescriptionQuery(id))
    }
}