package ru.nstu.isma.intg.lib.rungeKutta.rk3

import ru.nstu.isma.intg.api.methods.IIntegrationMethodFactory
import ru.nstu.isma.intg.api.methods.IntegrationMethodRungeKutta
import ru.nstu.isma.intg.lib.rungeKutta.rk3.internal.*

/**
 * @author Mariya Nasyrova
 * @since 04.10.14
 */

class IntegrationMethodFactory : IIntegrationMethodFactory {
    override val name = "Runge-Kutta 3"

    override fun create() = IntegrationMethodRungeKutta(
        accuracyController = Rk3AccuracyIntgController(),
        stabilityController = Rk3StabilityIntgController(),
        stageCalculators = arrayOf(
            Rk3Stage1Calculator(),
            Rk3Stage2Calculator(),
            Rk3Stage3Calculator(),
        ),
        nextY = {step, k, y, f -> y + 1.0 / 6.0 * k[0] + 2.0 / 3.0 * k[1] + 1.0 / 6.0 * k[2]}
    )
}