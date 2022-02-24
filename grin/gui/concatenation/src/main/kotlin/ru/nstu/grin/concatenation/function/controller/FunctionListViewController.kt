package ru.nstu.grin.concatenation.function.controller

import javafx.scene.Scene
import javafx.stage.Modality
import javafx.stage.Stage
import javafx.stage.Window
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.javafx.JavaFx
import kotlinx.coroutines.launch
import ru.nstu.grin.concatenation.canvas.model.ConcatenationCanvasModel
import ru.nstu.grin.concatenation.function.model.ConcatenationFunction
import ru.nstu.grin.concatenation.function.model.FunctionListViewModel
import ru.nstu.grin.concatenation.function.service.FunctionCanvasService
import ru.nstu.grin.concatenation.function.view.ChangeFunctionFragment
import ru.nstu.grin.concatenation.function.view.CopyFunctionFragment
import tornadofx.Controller
import java.util.*

class FunctionListViewController : Controller() {
    private val functionCanvasService: FunctionCanvasService by inject()
    private val concatenationCanvasModel: ConcatenationCanvasModel by inject()
    private val coroutineScope = CoroutineScope(Dispatchers.JavaFx)
    private val model: FunctionListViewModel by inject()

    init {
        coroutineScope.launch {
            concatenationCanvasModel.functionsListUpdatedEvent.collect{
                model.functions.setAll(it)
            }
        }

        model.functions.setAll(concatenationCanvasModel.getAllFunctions())
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

    fun openChangeModal(id: UUID, owner: Window? = null) {
        val view = find<ChangeFunctionFragment>(
            mapOf(
                ChangeFunctionFragment::functionId to id
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

    fun deleteFunction(function: ConcatenationFunction) {
        functionCanvasService.deleteFunction(function)
    }
}