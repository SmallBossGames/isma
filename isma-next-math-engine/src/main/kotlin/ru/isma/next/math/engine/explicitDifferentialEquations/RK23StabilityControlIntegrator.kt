package ru.isma.next.math.engine.explicitDifferentialEquations

import ru.isma.next.math.engine.shared.NonStationaryODE
import ru.isma.next.math.engine.shared.zeroSafetyNorm
import kotlin.math.*

private const val g = 1.0 / 16.0
private const val alpha2 = 1.0/3.0
private const val beta21 = 1.0/3.0
private const val beta31 = 7.0/18.0
private const val beta32 = 7.0/18.0
private const val alpha3 = 7.0/9.0
private const val p1 = 1.0/7.0
private const val p2 = 3.0/8.0
private const val p3 = 27.0/56.0

private const val v = 1e-7

class RK23StabilityControlIntegrator(val accuracy: Double)
    : ExplicitIntegrator()
{
    fun integrate(
        startTime: Double,
        endTime: Double,
        defaultStepSize: Double,
        y0: DoubleArray,
        rVector: DoubleArray,
        outY: DoubleArray,
        equations: NonStationaryODE
    ) : IExplicitMethodStatistic {
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
        statistic.evaluationsCount++

        while (time < endTime) {
            checkStepCount(statistic.stepsCount)

            step = normalizeStep(step, time, endTime)

            for (i in fLastBuffer.indices) {
                k1[i] = step * fLastBuffer[i]
                yNextBuffer[i] = outY[i] + beta21 * k1[i]
            }

            equations(time + alpha2, yNextBuffer, fCurrentBuffer)
            statistic.evaluationsCount++

            for (i in fCurrentBuffer.indices) {
                k2[i] = step * fCurrentBuffer[i]
                yNextBuffer[i] = outY[i] + beta31 * k1[i] + beta32 * k2[i]
            }

            equations(time + alpha3, yNextBuffer, fCurrentBuffer)
            statistic.evaluationsCount++

            for (i in fCurrentBuffer.indices) {
                k3[i] = step * fCurrentBuffer[i]
                yNextBuffer[i] = outY[i] + p1 * k1[i] + p2 * k2[i] + p3 * k3[i]
            }
            
            equations(time + step, yNextBuffer, fCurrentBuffer)
            statistic.evaluationsCount++

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

            for (i in fLastBuffer.indices){
                vectorBuffer1[i] = fCurrentBuffer[i] - fLastBuffer[i]
            }

            val q2 = ((6.0*accuracy / (abs(1.0 - 6.0* g)*step)) / zeroSafetyNorm(vectorBuffer1, outY, rVector))
                .pow(1.0/3.0)

            val r = findR(k1, k2, k3)

            if (q2 < 1.0 && r < 1.0 && !state.isLowStepSizeReached && !state.isHighStepSizeReached) {
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

            step = if(q2 < 1) min(q1, q2)*step else max(step, min(q1, min(q2, r))*step)

            state.isLowStepSizeReached = isLowStepSizeReached(step)
            state.isHighStepSizeReached = isHighStepSizeReached(step)

            val temp = fLastBuffer
            fLastBuffer = fCurrentBuffer
            fCurrentBuffer = temp

            currentEvaluationsCount++
        }
        return statistic
    }

    private fun findR(k1: DoubleArray, k2: DoubleArray, k3: DoubleArray) : Double {
        var max = 0.0
        for (i in k1.indices){
            max = max(abs((k3[i] - k2[i] + v) / (k2[i] - k1[i] + v)), max)
        }
        return 2.0/max
    }
}