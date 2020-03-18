package ru.nstu.grin.concatenation.marks

interface MarksProvider {
    fun getNextMark(zeroPoint: Double, current: Double, step: Double): String
}