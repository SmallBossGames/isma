package ru.nstu.grin.concatenation.function.transform

import ru.nstu.grin.common.model.Point

interface Transform {
    fun transform(point: Point): Point?
}