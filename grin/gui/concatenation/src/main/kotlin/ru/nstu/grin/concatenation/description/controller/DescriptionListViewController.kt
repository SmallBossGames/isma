package ru.nstu.grin.concatenation.description.controller

import javafx.scene.Scene
import javafx.stage.Modality
import javafx.stage.Stage
import javafx.stage.Window
import org.koin.core.component.get
import org.koin.core.parameter.parametersOf
import ru.nstu.grin.concatenation.description.model.Description
import ru.nstu.grin.concatenation.canvas.model.ConcatenationCanvasModel
import ru.nstu.grin.concatenation.description.model.DescriptionModalForUpdate
import ru.nstu.grin.concatenation.description.service.DescriptionCanvasService
import ru.nstu.grin.concatenation.description.view.ChangeDescriptionView
import ru.nstu.grin.concatenation.koin.DescriptionChangeModalScope
import ru.nstu.grin.concatenation.koin.MainGrinScope

class DescriptionListViewController(
    private val mainGrinScope: MainGrinScope,
    private val service: DescriptionCanvasService,
    private val canvasModel: ConcatenationCanvasModel,
) {
    fun openChangeModal(description: Description, window: Window? = null) {
        val descriptionChangeModalScope = mainGrinScope.get<DescriptionChangeModalScope>()

        val initData = DescriptionModalForUpdate(
            cartesianSpace = canvasModel.cartesianSpaces.first { it.descriptions.contains(description) },
            description = description,
        )

        val view = descriptionChangeModalScope.get<ChangeDescriptionView> { parametersOf(initData) }

        Stage().apply {
            scene = Scene(view)
            title = "Change Description"
            initModality(Modality.WINDOW_MODAL)

            if(window != null){
                initOwner(window)
            }

            setOnCloseRequest {
                descriptionChangeModalScope.scope.close()
            }

            show()
        }
    }

    fun deleteDescription(description: Description) {
        service.delete(description)
    }
}