package ru.nstu.grin.concatenation.points.events

import ru.nstu.grin.common.model.Point
import ru.nstu.grin.concatenation.points.model.AddFunctionsMode
import tornadofx.FXEvent

class FileCheckedEvent(
    val points: List<List<Point>>,
    val addFunctionsMode: AddFunctionsMode
) : FXEvent()