package ru.nstu.grin.concatenation.cartesian.controller

import javafx.scene.Scene
import javafx.stage.Modality
import javafx.stage.Stage
import javafx.stage.Window
import ru.nstu.grin.concatenation.cartesian.events.DeleteCartesianSpaceQuery
import ru.nstu.grin.concatenation.cartesian.model.CartesianSpace
import ru.nstu.grin.concatenation.cartesian.view.ChangeCartesianFragment
import ru.nstu.grin.concatenation.cartesian.view.CopyCartesianFragment
import tornadofx.Controller

class CartesianListViewController() : Controller() {
    fun openCopyModal(cartesianSpace: CartesianSpace, window: Window? = null) {
        val view = find<CopyCartesianFragment>(
            mapOf(
                CopyCartesianFragment::cartesianId to cartesianSpace.id
            )
        )

        Stage().apply {
            scene = Scene(view.root)
            title = "Function parameters"
            initModality(Modality.WINDOW_MODAL)

            if (window != null){
                initOwner(window)
            }

            show()
        }
    }

    fun openEditModal(cartesianSpace: CartesianSpace, window: Window? = null){
        val view = find<ChangeCartesianFragment>(
            mapOf(
                ChangeCartesianFragment::cartesianId to cartesianSpace.id
            )
        )

        Stage().apply {
            scene = Scene(view.root)
            title = "Function parameters"
            initModality(Modality.WINDOW_MODAL)

            if (window != null){
                initOwner(window)
            }

            show()
        }
    }

    fun deleteCartesian(cartesianSpace: CartesianSpace) {
        val event = DeleteCartesianSpaceQuery(cartesianSpace.id)
        fire(event)
    }
}