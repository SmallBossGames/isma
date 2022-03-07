package ru.nstu.grin.concatenation.cartesian.controller

import javafx.scene.Scene
import javafx.stage.Modality
import javafx.stage.Stage
import javafx.stage.Window
import org.koin.core.component.get
import org.koin.core.parameter.parametersOf
import ru.nstu.grin.concatenation.cartesian.model.CartesianSpace
import ru.nstu.grin.concatenation.cartesian.service.CartesianCanvasService
import ru.nstu.grin.concatenation.cartesian.view.ChangeCartesianFragment
import ru.nstu.grin.concatenation.cartesian.view.CopyCartesianFragment
import ru.nstu.grin.concatenation.koin.CartesianChangeModalScope
import ru.nstu.grin.concatenation.koin.CartesianCopyModalScope
import ru.nstu.grin.concatenation.koin.MainGrinScope

class CartesianListViewController(
    private val cartesianCanvasService: CartesianCanvasService,
    private val mainGrinScope: MainGrinScope,
) {
    fun openCopyModal(cartesianSpace: CartesianSpace, window: Window? = null) {
        val cartesianCopyModalScope = mainGrinScope.get<CartesianCopyModalScope>()
        val view = cartesianCopyModalScope.get<CopyCartesianFragment> { parametersOf(cartesianSpace) }

        Stage().apply {
            scene = Scene(view)
            title = "Function parameters"
            initModality(Modality.WINDOW_MODAL)

            if (window != null){
                initOwner(window)
            }

            setOnCloseRequest {
                cartesianCopyModalScope.closeScope()
            }

            show()
        }
    }

    fun openEditModal(cartesianSpace: CartesianSpace, window: Window? = null){
        val cartesianChangeModalScope = mainGrinScope.get<CartesianChangeModalScope>()
        val view = cartesianChangeModalScope.get<ChangeCartesianFragment> { parametersOf(cartesianSpace) }

        Stage().apply {
            scene = Scene(view)
            title = "Function parameters"
            initModality(Modality.WINDOW_MODAL)

            if (window != null){
                initOwner(window)
            }

            setOnCloseRequest {
                cartesianChangeModalScope.closeScope()
            }

            show()
        }
    }

    fun deleteCartesian(cartesianSpace: CartesianSpace) {
        cartesianCanvasService.deleteCartesianSpace(cartesianSpace)
    }
}