package ru.isma.next.math.engine.explicitDifferentialEquations

import ru.isma.next.math.common.NonStationaryODE
import ru.isma.next.math.engine.shared.zeroSafetyNorm
import kotlin.math.sqrt


class EulerIntegrator(val accuracy: Double) : ExplicitIntegrator() {
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
        var fNextBuffer = DoubleArray(y0.size)

        val yNextBuffer = DoubleArray(y0.size)

        val vectorBuffer = DoubleArray(y0.size)

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

        equations(time, outY, fCurrentBuffer)

        while (time < endTime){
            checkStepCount(statistic.stepsCount)
            step = normalizeStep(step, time, endTime)

            for (i in yNextBuffer.indices){
                yNextBuffer[i] = outY[i] + step*fCurrentBuffer[i]
            }

            equations(time + step, yNextBuffer, fNextBuffer)

            statistic.evaluationsCount++

            for (i in vectorBuffer.indices){
                vectorBuffer[i] = fNextBuffer[i] - fCurrentBuffer[i]
            }

            val errNorm = 0.5 * step * zeroSafetyNorm(vectorBuffer, outY, rVector)
            val q = sqrt(accuracy / errNorm)

            if(q < 1.0 && !state.isLowStepSizeReached && !state.isHighStepSizeReached){
                step = q * step / 1.1

                state.isLowStepSizeReached = isLowStepSizeReached(step)
                state.isHighStepSizeReached = isHighStepSizeReached(step)
                statistic.returnsCount++
            }
            else {
                for (i in outY.indices){
                    outY[i] = yNextBuffer[i]
                }

                time += step

                statistic.stepsCount++

                executeStepHandlers(time, outY, state, statistic)

                step = q * step / 1.1

                state.isLowStepSizeReached = isLowStepSizeReached(step)
                state.isHighStepSizeReached = isHighStepSizeReached(step)

                val temp = fCurrentBuffer
                fCurrentBuffer = fNextBuffer
                fNextBuffer = temp
            }
        }
        return statistic
    }

}