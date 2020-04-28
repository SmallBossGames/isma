package ru.nstu.grin.concatenation.axis.marks

interface MarksProvider {
    fun getNextMark(current: Double, zeroPoint: Double, currentStep: Double, step: Double): String
}