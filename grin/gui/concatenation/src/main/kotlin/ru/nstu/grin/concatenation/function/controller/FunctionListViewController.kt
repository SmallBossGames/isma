package ru.nstu.grin.concatenation.function.controller

import javafx.scene.Scene
import javafx.stage.Modality
import javafx.stage.Stage
import javafx.stage.Window
import org.koin.core.component.get
import org.koin.core.parameter.parametersOf
import ru.nstu.grin.concatenation.function.model.ConcatenationFunction
import ru.nstu.grin.concatenation.function.service.FunctionCanvasService
import ru.nstu.grin.concatenation.function.view.ChangeFunctionFragment
import ru.nstu.grin.concatenation.function.view.CopyFunctionFragment
import ru.nstu.grin.concatenation.koin.FunctionChangeModalScope
import ru.nstu.grin.concatenation.koin.FunctionCopyModalScope
import ru.nstu.grin.concatenation.koin.MainGrinScope

class FunctionListViewController(
    private val mainGrinScope: MainGrinScope,
    private val functionCanvasService: FunctionCanvasService
) {
    fun openCopyModal(function: ConcatenationFunction, window: Window? = null) {
        val functionCopyModalScope = mainGrinScope.get<FunctionCopyModalScope>()

        val view = functionCopyModalScope.get<CopyFunctionFragment> { parametersOf(function) }

        Stage().apply {
            scene = Scene(view)
            title = "Function parameters"
            initModality(Modality.WINDOW_MODAL)

            if (window != null){
                initOwner(window)
            }

            setOnCloseRequest {
                functionCopyModalScope.closeScope()
            }

            show()
        }
    }

    fun openChangeModal(function: ConcatenationFunction, window: Window? = null) {
        val functionChangeModalScope = mainGrinScope.get<FunctionChangeModalScope>()

        val view = functionChangeModalScope.get<ChangeFunctionFragment> { parametersOf(function) }

        Stage().apply {
            scene = Scene(view)
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