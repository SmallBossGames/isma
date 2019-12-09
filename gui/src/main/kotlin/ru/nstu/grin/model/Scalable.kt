package ru.nstu.grin.model

interface Scalable {
    fun scale(scale: Double, direction: CoordinateDirection): Drawable
}