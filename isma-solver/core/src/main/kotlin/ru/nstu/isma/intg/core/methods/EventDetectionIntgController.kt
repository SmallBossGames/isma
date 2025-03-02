package ru.nstu.isma.intg.core.methods

import ru.nstu.isma.intg.api.calcmodel.DaeSystem
import ru.nstu.isma.intg.api.calcmodel.EventFunction
import ru.nstu.isma.intg.api.calcmodel.EventFunctionGroup
import ru.nstu.isma.intg.api.calcmodel.EventFunctionGroup.StepChoiceRule
import ru.nstu.isma.intg.api.methods.IntgController
import ru.nstu.isma.intg.api.methods.IntgPoint
import ru.nstu.isma.intg.core.methods.utils.maxOrThrow
import ru.nstu.isma.intg.core.methods.utils.minOrThrow

/**
 * @author Maria
 * @since 18.05.2016
 */
class EventDetectionIntgController(private val gamma: Double) : IntgController() {

    init {
        require(!(gamma < 0.0 || gamma > 1.0)) { "Gamma should be >= 0 and <= 1." }
    }

    fun predictNextStep(intgPoint: IntgPoint, eventFunctionGroups: List<EventFunctionGroup>): Double {
        val steps = DoubleArray(eventFunctionGroups.size)

        for ((i, eventFunctionGroup) in eventFunctionGroups.withIndex()) {
            steps[i] = predictNextStep(intgPoint, eventFunctionGroup)
        }

        return steps.minOrThrow()
    }

    fun predictNextStep(intgPoint: IntgPoint, eventFunctionGroup: EventFunctionGroup): Double {
        val eventFunctions = eventFunctionGroup.eventFunctions
        val eventFunctionCount = eventFunctions.size

        val steps = DoubleArray(eventFunctionCount)

        for (i in 0 until eventFunctionCount) {
            val eventFunction = eventFunctions[i]

            // Вычисляем значение событийной функции для y.
            val g = eventFunction.apply(intgPoint.y, intgPoint.rhs)

            if (g > 0) {
                steps[i] = Double.MAX_VALUE
            } else {
                val DgDyAndRhsProduct = calculateDgDyAndRhsProduct(intgPoint, eventFunction, g)
                val DgDt = calculateDgDt(intgPoint, eventFunction, g)

                val step = (gamma - 1.0) * g / (DgDyAndRhsProduct + DgDt)
                steps[i] = if (step > 0) step else Double.MAX_VALUE
            }
        }
        val predictedStep = when (eventFunctionGroup.stepChoiceRule) {
            StepChoiceRule.MAX -> steps.maxOrThrow()
            StepChoiceRule.MIN -> steps.minOrThrow()
            else -> steps[0] // TODO: для одного значения переделать
        }
        return if (predictedStep > 0) predictedStep else intgPoint.nextStep // TODO: отрефакторить, более не актуально
    }

    private fun calculateDgDyAndRhsProduct(intgPoint: IntgPoint, eventFunction: EventFunction, g: Double): Double {
        val y = intgPoint.y // Массив y на текущем шаге интегрирования.
        val length = y.size

        val yInc = DoubleArray(length) // Массив y с приращенными значениями.
        for (i in 0 until length) {
            yInc[i] = y[i] + INCREMENT
        }

        val yToInc = y.copyOf(length) // Массив y для приращения.

        val DgDy = DoubleArray(length) // Частная производная от событийной функции по переменной y.
        var DgDyAndRhsProduct = 0.0 // Произведение DgDy на правую часть.

        // В цикле делаем рассчет, выполняя приращение значений в y по одному.
        for (i in 0 until length) {
            yToInc[i] = yInc[i] // Заменяем одно значение на приращенное.

            val gInc = eventFunction.apply(yToInc, intgPoint.rhs)
            DgDy[i] = (gInc - g) / INCREMENT

            DgDyAndRhsProduct += DgDy[i] * intgPoint.rhs[DaeSystem.RHS_DE_PART_IDX][i]

            yToInc[i] = y[i] // Возвращаем значение назад.
        }

        return DgDyAndRhsProduct
    }

    private fun calculateDgDt(intgPoint: IntgPoint, eventFunction: EventFunction, g: Double): Double {
        val gInc = eventFunction.apply(intgPoint.y, intgPoint.rhs)
        val DgDt = (gInc - g) / INCREMENT
        return DgDt
    }

    companion object {
        // Значение, которое используется при вычислении частных производных.
        private const val INCREMENT = 0.00001
    }
}
