package ru.isma.next.math.engine.explicitDifferentialEquations

import ru.isma.next.math.engine.shared.NonStationaryODE
import ru.isma.next.math.engine.shared.zeroSafetyNorm
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow

private const val g = 1.0 / 16.0
private const val alpha2 = 1.0/3.0
private const val beta21 = 1.0/3.0
private const val beta31 = 7.0/18.0
private const val beta32 = 7.0/18.0
private const val alpha3 = 7.0/9.0
private const val p1 = 1.0/7.0
private const val p2 = 3.0/8.0
private const val p3 = 27.0/56.0

class RK23Integrator(val accuracy: Double) : ExplicitIntegrator() {
    fun integrate(
        startTime: Double,
        endTime: Double,
        defaultStepSize: Double,
        y0: DoubleArray,
        rVector: DoubleArray,
        outY: DoubleArray,
        equations: NonStationaryODE
    ) : ExplicitMethodStatistic {
        y0.copyInto(outY)

        var fCurrentBuffer = DoubleArray(y0.size)
        var fLastBuffer = DoubleArray(y0.size)
        var currentEvaluationsCount = 0

        val yNextBuffer = DoubleArray(y0.size)
        val vectorBuffer1 = DoubleArray(y0.size)

        val k1 = DoubleArray(y0.size)
        val k2 = DoubleArray(y0.size)
        val k3 = DoubleArray(y0.size)

        var time = startTime
        var step = defaultStepSize

        val statistic = ExplicitMethodStatistic(
            stepsCount = 0,
            evaluationsCount = 0,
            returnsCount = 0
        )

        val state = ExplicitMethodStepState(
            isLowStepSizeReached = isLowStepSizeReached(step),
            isHighStepSizeReached = isHighStepSizeReached(step)
        )

        executeStepHandlers(time, outY, state, statistic)

        equations(time, outY, fLastBuffer)

        while (time < endTime){
            step = normalizeStep(step, time, endTime)

            for (i in fLastBuffer.indices) {
                k1[i] = step * fLastBuffer[i]
                yNextBuffer[i] = outY[i] + beta21 * k1[i]
            }

            equations(time + alpha2 * step, yNextBuffer, fCurrentBuffer)
            statistic.evaluationsCount++

            for (i in fCurrentBuffer.indices) {
                k2[i] = step * fCurrentBuffer[i]
                yNextBuffer[i] = outY[i] + beta31 * k1[i] + beta32 * k2[i]
            }

            equations(time + alpha3 * step, yNextBuffer, fCurrentBuffer)
            statistic.evaluationsCount++

            for (i in fCurrentBuffer.indices) {
                k3[i] = step * fCurrentBuffer[i]
                yNextBuffer[i] = outY[i] + p1 * k1[i] + p2 * k2[i] + p3 * k3[i]
            }

            for (i in fLastBuffer.indices){
                vectorBuffer1[i] = k2[i] - k1[i]
            }

            val q1 = ((6.0 * abs(alpha2) * accuracy / abs(1.0 - 6.0* g)) / zeroSafetyNorm(vectorBuffer1, outY, rVector))
                .pow(1.0/3.0)

            if (q1 < 1.0 && !state.isLowStepSizeReached && !state.isHighStepSizeReached) {
                step = q1 * step / 1.1

                state.isLowStepSizeReached = isLowStepSizeReached(step)
                state.isHighStepSizeReached = isHighStepSizeReached(step)
                statistic.returnsCount++

                continue
            }

            equations(time + step, yNextBuffer, fCurrentBuffer)
            statistic.evaluationsCount++

            for (i in fLastBuffer.indices){
                vectorBuffer1[i] = fCurrentBuffer[i] - fLastBuffer[i]
            }

            val q2 = ((6.0*accuracy / (abs(1.0 - 6.0* g)*step)) / zeroSafetyNorm(vectorBuffer1, outY, rVector))
                .pow(1.0/3.0)

            if (q2 < 1.0 && !state.isLowStepSizeReached && !state.isHighStepSizeReached) {
                step = q2 * step / 1.1

                state.isLowStepSizeReached = isLowStepSizeReached(step)
                state.isHighStepSizeReached = isHighStepSizeReached(step)
                statistic.returnsCount++

                continue
            }

            for (i in yNextBuffer.indices) {
                outY[i] = yNextBuffer[i]
            }

            time += step

            statistic.stepsCount++

            executeStepHandlers(time, outY, state, statistic)

            step = max(step, min(q1, q2)*step)

            state.isLowStepSizeReached = isLowStepSizeReached(step)
            state.isHighStepSizeReached = isHighStepSizeReached(step)

            val temp = fLastBuffer
            fLastBuffer = fCurrentBuffer
            fCurrentBuffer = temp

            currentEvaluationsCount++
        }
        return statistic
    }
}