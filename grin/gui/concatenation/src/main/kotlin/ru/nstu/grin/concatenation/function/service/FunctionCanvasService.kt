package ru.nstu.grin.concatenation.function.service

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.nstu.grin.concatenation.axis.converter.ConcatenationAxisConverter
import ru.nstu.grin.concatenation.canvas.converter.CartesianSpaceConverter
import ru.nstu.grin.concatenation.canvas.dto.CartesianSpaceDTO
import ru.nstu.grin.concatenation.canvas.model.ConcatenationCanvasModel
import ru.nstu.grin.concatenation.function.converter.ConcatenationFunctionConverter
import ru.nstu.grin.concatenation.function.model.ConcatenationFunction
import ru.nstu.grin.concatenation.function.model.UpdateFunctionData
import ru.nstu.grin.concatenation.function.transform.IAsyncPointsTransformer

class FunctionCanvasService(
    private val model: ConcatenationCanvasModel,
) {
    private val coroutineScope = CoroutineScope(Dispatchers.Default)

    fun addFunction(cartesianSpace: CartesianSpaceDTO) {
        val found = model.cartesianSpaces.firstOrNull {
            it.xAxis.name == cartesianSpace.xAxis.name
                && it.yAxis.name == cartesianSpace.yAxis.name
        }
        if (found == null) {
            val xAxis = cartesianSpace.xAxis.let { ConcatenationAxisConverter.merge(it) }
            val yAxis = cartesianSpace.yAxis.let { ConcatenationAxisConverter.merge(it) }
            val added = CartesianSpaceConverter.merge(cartesianSpace, xAxis, yAxis)
            model.cartesianSpaces.add(added)
        } else {
            model.cartesianSpaces.remove(found)

            val functions = cartesianSpace.functions.map { ConcatenationFunctionConverter.convert(it) }
            found.merge(functions)
            model.cartesianSpaces.add(found)
        }
    }

    fun copyFunction(originFunction: ConcatenationFunction, newName: String = originFunction.name) {
        val newFunction = originFunction.copy(name = newName)
        val cartesianSpace = model.cartesianSpaces.first { it.functions.contains(originFunction) }
        cartesianSpace.functions.add(newFunction)

        coroutineScope.launch {
            model.reportFunctionsListUpdate()
        }
    }

    fun updateFunction(event: UpdateFunctionData) {
        event.function.apply {
            name = event.name
            functionColor = event.color
            isHide = event.isHide
            lineSize = event.lineSize
            lineType = event.lineType
        }

        coroutineScope.launch {
            model.reportFunctionsListUpdate()
        }
    }

    fun updateTransformer(
        function: ConcatenationFunction,
        operation: (Array<IAsyncPointsTransformer>) -> Array<IAsyncPointsTransformer>
    ) = coroutineScope.launch {
        do {
            if (function.updateTransformersTransaction(operation)){
                break
            }
        } while (true)

        model.reportFunctionsListUpdate()
    }

    fun updateTransformer(
        function: ConcatenationFunction,
        transformers: Array<IAsyncPointsTransformer>
    ) = coroutineScope.launch {
        do {
            if (function.updateTransformersTransaction{ transformers }){
                break
            }
        } while (true)

        model.reportFunctionsListUpdate()
    }

    inline fun <reified T: IAsyncPointsTransformer> addOrUpdateLastTransformer(
        function: ConcatenationFunction,
        noinline operation: (T?) -> T
    ) {
        updateTransformer(function){ transformers ->
            val last = transformers.lastOrNull()

            if(last is T){
                transformers[transformers.size - 1] = operation(last)

                transformers
            } else {
                arrayOf(*transformers, operation(null))
            }
        }
    }

    fun deleteFunction(function: ConcatenationFunction) {
        model.cartesianSpaces.forEach {
            it.functions.remove(function)
        }

        coroutineScope.launch {
            model.reportFunctionsListUpdate()
        }
    }

}