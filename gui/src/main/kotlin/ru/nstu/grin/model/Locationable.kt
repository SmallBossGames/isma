package ru.nstu.grin.model

interface Locationable {

    fun isOnIt(x: Double, y: Double): Boolean
}