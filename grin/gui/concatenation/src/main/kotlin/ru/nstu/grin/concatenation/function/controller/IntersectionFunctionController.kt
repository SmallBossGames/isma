package ru.nstu.grin.concatenation.function.controller

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ru.nstu.grin.concatenation.function.model.IntersectionFunctionViewModel
import ru.nstu.grin.concatenation.function.service.FunctionOperationsService

class IntersectionFunctionController(
    private val functionCanvasService: FunctionOperationsService,
) {
    private val coroutineScope = CoroutineScope(Dispatchers.Default)

    fun findIntersection(viewModel: IntersectionFunctionViewModel) {
        coroutineScope.launch {
            functionCanvasService.showInterSections(
                viewModel.selectedFunctions[0],
                viewModel.selectedFunctions[1],
                viewModel.mergeIntervalsDistance
            )
        }
    }
}