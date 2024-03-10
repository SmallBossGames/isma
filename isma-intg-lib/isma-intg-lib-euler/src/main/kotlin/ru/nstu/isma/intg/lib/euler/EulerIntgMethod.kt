package ru.nstu.isma.intg.lib.euler

import ru.nstu.isma.intg.api.methods.IIntegrationMethodFactory
import ru.nstu.isma.intg.api.methods.IntegrationMethodRungeKutta
import ru.nstu.isma.intg.api.methods.IntgMethod
import ru.nstu.isma.intg.api.methods.StageCalculator

/**
 * @author Mariya Nasyrova
 * @since 04.10.14
 */
class EulerIntgMethod : IntgMethod {
    override val accuracyController = null

    override val stabilityController = null

    override val stageCalculators = emptyArray<StageCalculator>()

    override fun nextY(step: Double, k: DoubleArray, y: Double, f: Double) = y + step * f
}

class IntegrationMethodFactory : IIntegrationMethodFactory{
    override val name = "Euler"

    override fun create() = EulerIntgMethod()

    override fun createNg() = IntegrationMethodRungeKutta(
        nextY = { step, k, y, f -> y + step * f }
    )
}