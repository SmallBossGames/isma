package ru.nstu.grin.concatenation.model.function

import ru.nstu.grin.common.model.Point
import ru.nstu.grin.concatenation.model.AddFunctionsMode

class FileFunctionViewModel : AbstractAddFunctionModel() {
    var points: List<List<Point>>? = null
    var addFunctionsMode: AddFunctionsMode? = null
}