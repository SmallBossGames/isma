package ru.nstu.isma.intg.lib.rungeKutta.rk22

import ru.nstu.isma.intg.api.methods.*
import ru.nstu.isma.intg.lib.rungeKutta.rk22.internal.Rk2AccuracyIntgController
import ru.nstu.isma.intg.lib.rungeKutta.rk22.internal.Rk2StabilityIntgController
import ru.nstu.isma.intg.lib.rungeKutta.rk22.internal.Rk2Stage1Calculator
import ru.nstu.isma.intg.lib.rungeKutta.rk22.internal.Rk2Stage2Calculator

/**
 * @author Mariya Nasyrova
 * @since 01.09.14
 */
class IntegrationMethodFactory : IIntegrationMethodFactory {
    override val name = "Runge-Kutta 2"

    override fun create() = IntegrationMethodRungeKutta(
        accuracyController = Rk2AccuracyIntgController(),
        stabilityController = Rk2StabilityIntgController(),
        stageCalculators = arrayOf(
            Rk2Stage1Calculator(),
            Rk2Stage2Calculator(),
        ),
        nextY = {step, k, y, f -> y + 0.5 * k[0] + 0.5 * k[1]}
    )
}