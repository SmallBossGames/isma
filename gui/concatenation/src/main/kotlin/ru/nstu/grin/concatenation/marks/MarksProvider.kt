package ru.nstu.grin.concatenation.marks

interface MarksProvider {
    fun getNextMark(current: Double, zeroPoint: Double, currentStep: Double, step: Double): String
}