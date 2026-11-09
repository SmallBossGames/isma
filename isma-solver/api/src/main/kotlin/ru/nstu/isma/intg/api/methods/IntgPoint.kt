package ru.nstu.isma.intg.api.methods

class IntgPoint(
    /** Шаг интегрирования.  */
    var step: Double,
    /** Значение y, соответствующие step.  */
    var y: DoubleArray,
    /** Массив значений правой части системы уравнений, вычисленный по y.  */
    var rhs: Array<DoubleArray>,
    /** Значения вспомогательных стадий метода, использовавшихся для вычисления y.  */
    var stages: Array<DoubleArray> = emptyArray<DoubleArray>(),
    /** Величина следующего шага интегрирования.  */
    var nextStep: Double = step
) {
    fun copyLight(): IntgPoint {
        val newY = y.copyOf()
        val newRhs = rhs.map {it.copyOf()}.toTypedArray()

        return IntgPoint(step, newY, newRhs)
    }
}