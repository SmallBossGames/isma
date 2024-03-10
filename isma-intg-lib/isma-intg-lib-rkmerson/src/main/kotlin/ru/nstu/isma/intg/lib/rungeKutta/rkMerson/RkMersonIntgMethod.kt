package ru.nstu.isma.intg.lib.rungeKutta.rkMerson

import ru.nstu.isma.intg.api.methods.IIntegrationMethodFactory
import ru.nstu.isma.intg.api.methods.IntegrationMethodRungeKutta
import ru.nstu.isma.intg.api.methods.IntgMethod
import ru.nstu.isma.intg.lib.rungeKutta.rkMerson.internal.*

/**
 * @author Mariya Nasyrova
 * @since 04.10.14
 */
class RkMersonIntgMethod : IntgMethod {
    override val accuracyController = RkMersonAccuracyIntgController()
    override val stabilityController = null
    override val stageCalculators = arrayOf(
        RkMersonStage1Calculator(),
        RkMersonStage2Calculator(),
        RkMersonStage3Calculator(),
        RkMersonStage4Calculator(),
        RkMersonStage5Calculator(),
    )

    override fun nextY(step: Double, k: DoubleArray, y: Double, f: Double): Double {
        return y + (k[0] + 4.0 * k[3] + k[4]) / 6.0
    }
}

class IntegrationMethodFactory : IIntegrationMethodFactory {
    override val name = "Runge-Kutta-Merson"

    override fun create() = IntegrationMethodRungeKutta(
        accuracyController = RkMersonAccuracyIntgController(),
        stageCalculators = arrayOf(
        RkMersonStage1Calculator(),
        RkMersonStage2Calculator(),
        RkMersonStage3Calculator(),
        RkMersonStage4Calculator(),
        RkMersonStage5Calculator(),
    ),
        nextY = {step, k, y, f -> (k[0] + 4.0 * k[3] + k[4]) / 6.0}
    )
}
