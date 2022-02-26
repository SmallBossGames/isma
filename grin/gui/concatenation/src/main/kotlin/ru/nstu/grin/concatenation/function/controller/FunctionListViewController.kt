package ru.nstu.grin.concatenation.function.controller

import javafx.scene.Scene
import javafx.stage.Modality
import javafx.stage.Stage
import javafx.stage.Window
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.javafx.JavaFx
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.parameter.parametersOf
import ru.nstu.grin.concatenation.canvas.model.ConcatenationCanvasModel
import ru.nstu.grin.concatenation.function.model.ConcatenationFunction
import ru.nstu.grin.concatenation.function.model.FunctionListViewModel
import ru.nstu.grin.concatenation.function.service.FunctionCanvasService
import ru.nstu.grin.concatenation.function.view.ChangeFunctionFragment
import ru.nstu.grin.concatenation.function.view.CopyFunctionFragment
import ru.nstu.grin.concatenation.koin.FunctionChangeModalScope
import ru.nstu.grin.concatenation.koin.MainGrinScope
import tornadofx.Controller
import tornadofx.Scope

class FunctionListViewController(
    override val scope: Scope,
    private val model: FunctionListViewModel,
    private val mainGrinScope: MainGrinScope
) : Controller(), KoinComponent {
    private val coroutineScope = CoroutineScope(Dispatchers.JavaFx)
    private val functionCanvasService: FunctionCanvasService by inject()
    private val concatenationCanvasModel: ConcatenationCanvasModel by inject()

    init {
        coroutineScope.launch {
            merge(
                flowOf(concatenationCanvasModel.getAllFunctions()),
                concatenationCanvasModel.functionsListUpdatedEvent
            ).collect {
                model.functions.setAll(it)
            }
        }
    }

    fun openCopyModal(function: ConcatenationFunction) {
        val view = find<CopyFunctionFragment>(
            mapOf(
                "function" to function
            )
        )

        Stage().apply {
            scene = Scene(view.root)
            title = "Function parameters"
            initModality(Modality.WINDOW_MODAL)

            if (owner != null){
                initOwner(owner)
            }

            show()
        }
    }

    fun openChangeModal(function: ConcatenationFunction, owner: Window? = null) {
        val functionChangeModalScope = mainGrinScope.get<FunctionChangeModalScope>()

        val view = functionChangeModalScope.get<ChangeFunctionFragment> { parametersOf(function) }

        Stage().apply {
            scene = Scene(view.root)
            title = "Change Function"
            initModality(Modality.WINDOW_MODAL)

            if (owner != null){
                initOwner(owner)
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