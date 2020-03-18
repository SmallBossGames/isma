package ru.nstu.grin.concatenation.marks

class DoubleMarksProvider : MarksProvider {
    override fun getNextMark(zeroPoint: Double, current: Double, step: Double): String {
        return when {
            current > zeroPoint -> {
                (current + step).toString()
            }
            current < zeroPoint -> {
                (current - step).toString()
            }
            else -> {
                current.toString()
            }
        }
    }
}