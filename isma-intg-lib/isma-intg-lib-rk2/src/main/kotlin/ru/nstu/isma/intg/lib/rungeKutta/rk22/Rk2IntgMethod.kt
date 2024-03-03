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
class Rk2IntgMethod : IntgMethod {
    override val accuracyController = Rk2AccuracyIntgController()
    override val stabilityController = Rk2StabilityIntgController()
    override val stageCalculators: Array<StageCalculator> = arrayOf(
        Rk2Stage1Calculator(),
        Rk2Stage2Calculator(),
    )

    override fun nextY(step: Double, k: DoubleArray, y: Double, f: Double): Double {
        return y + 0.5 * k[0] + 0.5 * k[1]

        // Результат, как по Эйлеру 1-ый порядок точности
        //return y + 0.25 * k[0] + 0.75 * k[1];
    }
}

class IntegrationMethodFactory : IIntegrationMethodFactory {
    override val name = "Runge-Kutta 2"

    override fun create() = Rk2IntgMethod()
}