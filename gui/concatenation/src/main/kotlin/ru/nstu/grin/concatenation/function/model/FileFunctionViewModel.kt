package ru.nstu.grin.concatenation.function.model

import ru.nstu.grin.common.model.Point
import ru.nstu.grin.concatenation.points.model.AddFunctionsMode

class FileFunctionViewModel : AbstractAddFunctionModel() {
    var points: List<List<Point>>? = null
    var addFunctionsMode: AddFunctionsMode? = null
}