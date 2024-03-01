package ru.nstu.isma.intg.lib.euler

import ru.nstu.isma.intg.api.methods.IntgMethod
import ru.nstu.isma.intg.api.methods.StageCalculator

/**
 * @author Mariya Nasyrova
 * @since 04.10.14
 */
class EulerIntgMethod : IntgMethod {
    override val name = "Euler"

    override val accuracyController = null

    override val stabilityController = null

    override val stageCalculators = emptyArray<StageCalculator>()

    override fun nextY(step: Double, k: DoubleArray, y: Double, f: Double) = y + step * f
}