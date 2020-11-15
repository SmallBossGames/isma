package ru.nstu.grin.concatenation.description.controller

import ru.nstu.grin.concatenation.description.events.DeleteDescriptionQuery
import ru.nstu.grin.concatenation.description.events.GetAllDescriptionsEvent
import ru.nstu.grin.concatenation.description.model.DescriptionListViewModel
import ru.nstu.grin.concatenation.description.view.ChangeDescriptionFragment
import tornadofx.Controller
import java.util.*

class DescriptionListViewController : Controller() {
    private val model: DescriptionListViewModel by inject()

    init {
        subscribe<GetAllDescriptionsEvent> {
            if (model.descriptions != null) {
                model.descriptions.clear()
            }
            model.descriptionsProperty.setAll(it.descriptions)
        }
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