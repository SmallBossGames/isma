package ru.nstu.grin.concatenation.function.transform

import ru.nstu.grin.common.model.Point

class MirrorTransform(
    private val isMirrorX: Boolean,
    private val isMirrorY: Boolean
) : Transform {
    override fun transform(point: Point): Point? {
        val x = mirrorTransform(point.x, isMirrorX)
        val y = mirrorTransform(point.y, isMirrorY)
        return Point(x, y)
    }

    private fun mirrorTransform(number: Double, isMirror: Boolean): Double {
        return if (isMirror) {
            -number
        } else {
            number
        }
    }
}