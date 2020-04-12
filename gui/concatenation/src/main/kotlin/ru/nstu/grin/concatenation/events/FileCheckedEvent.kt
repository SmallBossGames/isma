package ru.nstu.grin.concatenation.events

import ru.nstu.grin.common.model.Point
import tornadofx.FXEvent

class FileCheckedEvent(
    val points: List<List<Point>>
) : FXEvent()