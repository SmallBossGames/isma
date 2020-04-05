package ru.nstu.grin.concatenation.marks

class InvertDoubleMarksProvider : MarksProvider {
    override fun getNextMark(current: Double, zeroPoint: Double, currentStep: Double, step: Double): String {
        return when {
            current < zeroPoint -> {
                (currentStep + step).toString()
            }
            current > zeroPoint -> {
                (currentStep - step).toString()
            }
            else -> {
                currentStep.toString()
            }
        }
    }
}