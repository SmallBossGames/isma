package ru.nstu.grin.concatenation.axis.controller

import javafx.scene.Scene
import javafx.stage.Modality
import javafx.stage.Stage
import javafx.stage.Window
import org.koin.core.component.get
import org.koin.core.parameter.parametersOf
import ru.nstu.grin.concatenation.axis.model.ConcatenationAxis
import ru.nstu.grin.concatenation.axis.view.AxisChangeFragment
import ru.nstu.grin.concatenation.koin.AxisChangeModalScope
import ru.nstu.grin.concatenation.koin.MainGrinScope

class AxisListViewController(
    private val mainGrinScope: MainGrinScope,
) {
    fun editAxis(axis: ConcatenationAxis, window: Window? = null){
        val scope = mainGrinScope.get<AxisChangeModalScope>()
        val view = scope.get<AxisChangeFragment>() { parametersOf(axis) }

        Stage().apply {
            scene = Scene(view)
            title = "Change Axis"

            initModality(Modality.WINDOW_MODAL)

            if(window != null){
                initOwner(window)
            }

            setOnCloseRequest {
                scope.scope.close()
            }

            show()
        }
    }
}