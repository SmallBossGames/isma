package ru.nstu.grin.concatenation.marks

class DoubleMarksProvider : MarksProvider {
    override fun getMark(current: Double, step: Double): String {
        return (current + step).toString()
    }
}