package ru.nstu.isma.intg.lib.rungeKutta.rk3

import ru.nstu.isma.intg.api.methods.IIntegrationMethodFactory
import ru.nstu.isma.intg.api.methods.IntgMethod
import ru.nstu.isma.intg.lib.rungeKutta.rk3.internal.*

/**
 * @author Mariya Nasyrova
 * @since 04.10.14
 */
class Rk3IntgMethod : IntgMethod {
    override val accuracyController = Rk3AccuracyIntgController()
    override val stabilityController = Rk3StabilityIntgController()
    override val stageCalculators = arrayOf(
        Rk3Stage1Calculator(),
        Rk3Stage2Calculator(),
        Rk3Stage3Calculator(),
    )


    override fun nextY(step: Double, k: DoubleArray, y: Double, f: Double): Double {
        return y + 1.0 / 6.0 * k[0] + 2.0 / 3.0 * k[1] + 1.0 / 6.0 * k[2]
    }
}

class IntegrationMethodFactory : IIntegrationMethodFactory {
    override val name = "Runge-Kutta 3"

    override fun create() = Rk3IntgMethod()
}