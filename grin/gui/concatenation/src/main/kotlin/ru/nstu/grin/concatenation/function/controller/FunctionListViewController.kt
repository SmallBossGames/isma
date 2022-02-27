package ru.nstu.grin.concatenation.function.controller

import javafx.scene.Scene
import javafx.stage.Modality
import javafx.stage.Stage
import javafx.stage.Window
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.parameter.parametersOf
import ru.nstu.grin.concatenation.function.model.ConcatenationFunction
import ru.nstu.grin.concatenation.function.service.FunctionCanvasService
import ru.nstu.grin.concatenation.function.view.ChangeFunctionFragment
import ru.nstu.grin.concatenation.function.view.CopyFunctionFragment
import ru.nstu.grin.concatenation.koin.FunctionChangeModalScope
import ru.nstu.grin.concatenation.koin.MainGrinScope
import tornadofx.Controller
import tornadofx.Scope

class FunctionListViewController(
    override val scope: Scope,
    private val mainGrinScope: MainGrinScope
) : Controller(), KoinComponent {
    private val functionCanvasService: FunctionCanvasService by inject()

    fun openCopyModal(function: ConcatenationFunction, window: Window? = null) {
        val view = find<CopyFunctionFragment>(
            mapOf(
                "function" to function
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

    fun openChangeModal(function: ConcatenationFunction, window: Window? = null) {
        val functionChangeModalScope = mainGrinScope.get<FunctionChangeModalScope>()

        val view = functionChangeModalScope.get<ChangeFunctionFragment> { parametersOf(function) }

        Stage().apply {
            scene = Scene(view.root)
            title = "Change Function"
            initModality(Modality.WINDOW_MODAL)

            if (window != null){
                initOwner(window)
            }

            setOnCloseRequest {
                functionChangeModalScope.closeScope()
            }

            show()
        }
    }

    fun deleteFunction(function: ConcatenationFunction) {
        functionCanvasService.deleteFunction(function)
    }
}