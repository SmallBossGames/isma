package ru.nstu.isma.intg.lib.rungeKutta.rk31

import ru.nstu.isma.intg.api.methods.IIntegrationMethodFactory
import ru.nstu.isma.intg.api.methods.IntegrationMethodRungeKutta
import ru.nstu.isma.intg.api.methods.IntgMethod
import ru.nstu.isma.intg.lib.rungeKutta.rk31.internal.*

/**
 * @author Dmitry Dostovalov
 * @since 16.10.15
 */

class IntegrationMethodFactory : IIntegrationMethodFactory {
    override val name = "Runge-Kutta 3-1"

    override fun create() = IntegrationMethodRungeKutta(
        accuracyController = Rk31AccuracyIntgController(),
        stageCalculators = arrayOf(
            Rk31Stage1Calculator(),
            Rk31Stage2Calculator(),
            Rk31Stage3Calculator(),
            Rk31Stage4Calculator(),
        ),
        nextY = {step, k, y, f -> y + 17.0 / 84.0 * k[0] + 27.0 / 20.0 * k[1] + 2.0 / 3.0 * k[2] - 128.0 / 105.0 * k[3]}
    )
}
