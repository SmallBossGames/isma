package ru.nstu.grin.concatenation.description.controller

import ru.nstu.grin.common.model.Description
import ru.nstu.grin.concatenation.description.events.DeleteDescriptionQuery
import ru.nstu.grin.concatenation.description.view.ChangeDescriptionFragment
import tornadofx.Controller
import tornadofx.Scope

class DescriptionListViewController(
    override val scope: Scope
) : Controller() {
    fun openChangeModal(description: Description) {
        find<ChangeDescriptionFragment>(
            mapOf(
                ChangeDescriptionFragment::descriptionId to description.id
            )
        ).openModal()
    }

    fun deleteDescription(description: Description) {
        fire(DeleteDescriptionQuery(description.id))
    }
}