package ru.nstu.grin.concatenation.model

interface Scalable {
    fun scale(scale: Double, direction: CoordinateDirection): Drawable
}