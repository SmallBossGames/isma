package ru.nstu.grin.concatenation.description.controller

import javafx.scene.Scene
import javafx.stage.Modality
import javafx.stage.Stage
import javafx.stage.Window
import ru.nstu.grin.common.model.Description
import ru.nstu.grin.concatenation.description.service.DescriptionCanvasService
import ru.nstu.grin.concatenation.description.view.ChangeDescriptionFragment
import tornadofx.Controller
import tornadofx.Scope

class DescriptionListViewController(
    override val scope: Scope
) : Controller() {
    private val service: DescriptionCanvasService by inject()

    fun openChangeModal(description: Description, window: Window? = null) {

        val view = find<ChangeDescriptionFragment>(
            mapOf(
                "description" to description
            )
        )

        Stage().apply {
            scene = Scene(view.root)
            title = "Change Description"
            initModality(Modality.WINDOW_MODAL)

            if(window != null){
                initOwner(window)
            }

            show()
        }
    }

    fun deleteDescription(description: Description) {
        service.delete(description)
    }
}