package ru.nstu.grin.simple.events

import ru.nstu.grin.common.model.Point
import tornadofx.FXEvent

/**
 * Используется после проверки точек в таблице
 */
class FileCheckedEvent(val points: List<Point>) : FXEvent()