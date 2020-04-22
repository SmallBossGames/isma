package ru.nstu.grin.concatenation.events

import ru.nstu.grin.common.model.Point
import ru.nstu.grin.concatenation.model.AddFunctionsMode
import ru.nstu.grin.concatenation.model.FileReaderMode
import tornadofx.FXEvent

class FileCheckedEvent(
    val points: List<List<Point>>,
    val addFunctionsMode: AddFunctionsMode,
    val fileReaderMode: FileReaderMode
) : FXEvent()