package ru.nstu.grin.concatenation.description.controller

import javafx.scene.Scene
import javafx.stage.Modality
import javafx.stage.Stage
import javafx.stage.Window
import org.koin.core.component.get
import org.koin.core.parameter.parametersOf
import ru.nstu.grin.common.model.Description
import ru.nstu.grin.concatenation.description.service.DescriptionCanvasService
import ru.nstu.grin.concatenation.description.view.ChangeDescriptionFragment
import ru.nstu.grin.concatenation.koin.DescriptionChangeModalScope
import ru.nstu.grin.concatenation.koin.MainGrinScope
import tornadofx.Controller
import tornadofx.Scope

class DescriptionListViewController(
    override val scope: Scope,
    private val mainGrinScope: MainGrinScope
) : Controller() {
    private val service: DescriptionCanvasService by inject()

    fun openChangeModal(description: Description, window: Window? = null) {
        val descriptionChangeModalScope = mainGrinScope.get<DescriptionChangeModalScope>()

        val view = descriptionChangeModalScope.get<ChangeDescriptionFragment> { parametersOf(description) }

        Stage().apply {
            scene = Scene(view)
            title = "Change Description"
            initModality(Modality.WINDOW_MODAL)

            if(window != null){
                initOwner(window)
            }

            setOnCloseRequest {
                descriptionChangeModalScope.closeScope()
            }

            show()
        }
    }

    fun deleteDescription(description: Description) {
        service.delete(description)
    }
}