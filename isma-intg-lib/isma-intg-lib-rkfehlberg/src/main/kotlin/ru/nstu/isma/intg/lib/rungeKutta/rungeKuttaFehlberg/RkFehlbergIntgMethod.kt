package ru.nstu.isma.intg.lib.rungeKutta.rungeKuttaFehlberg

import ru.nstu.isma.intg.api.methods.IIntegrationMethodFactory
import ru.nstu.isma.intg.api.methods.IntegrationMethodRungeKutta
import ru.nstu.isma.intg.api.methods.IntgMethod
import ru.nstu.isma.intg.lib.rungeKutta.rungeKuttaFehlberg.internal.*

class RkFehlbergIntgMethod : IntgMethod {
    override val accuracyController = RkFehlbergAccuracyIntgController()
    override val stabilityController = RkFehlbergStabilityIntgController()
    override var stageCalculators = arrayOf(
        RkFehlbergStage1Calculator(),
        RkFehlbergStage2Calculator(),
        RkFehlbergStage3Calculator(),
        RkFehlbergStage4Calculator(),
        RkFehlbergStage5Calculator(),
        RkFehlbergStage6Calculator(),
    )

    override fun nextY(step: Double, k: DoubleArray, y: Double, f: Double): Double {
        return (y + 0.1185185185 * k[0] + 0.5189863548 * k[2] + 0.5061314903 * k[3] - 0.18 * k[4]
                + 0.0363636363 * k[5])
    }
}

class IntegrationMethodFactory : IIntegrationMethodFactory {
    override val name = "Runge-Kutta-Fehlberg"

    override fun create() = IntegrationMethodRungeKutta(
        accuracyController = RkFehlbergAccuracyIntgController(),
        stabilityController = RkFehlbergStabilityIntgController(),
        stageCalculators = arrayOf(
            RkFehlbergStage1Calculator(),
            RkFehlbergStage2Calculator(),
            RkFehlbergStage3Calculator(),
            RkFehlbergStage4Calculator(),
            RkFehlbergStage5Calculator(),
            RkFehlbergStage6Calculator(),
        ),
        nextY = {step, k, y, f -> (y + 0.1185185185 * k[0] + 0.5189863548 * k[2] + 0.5061314903 * k[3] - 0.18 * k[4]
                + 0.0363636363 * k[5])}
    )
}
