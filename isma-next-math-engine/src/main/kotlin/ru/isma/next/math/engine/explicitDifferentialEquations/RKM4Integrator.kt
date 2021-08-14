package ru.isma.next.math.engine.explicitDifferentialEquations

import ru.isma.next.math.engine.shared.NonStationaryODE
import kotlin.math.abs

class RKM4Integrator(val accuracy: Double) : ExplicitIntegrator() {
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

        val fCurrentBuffer = DoubleArray(y0.size)
        val yNextBuffer = DoubleArray(y0.size)
        val vectorBuffer1 = DoubleArray(y0.size)

        val k = Array(5){ DoubleArray(y0.size) }

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

        mainLoop@ while (time < endTime) {
            step = normalizeStep(step, time, endTime)

            equations(time, outY, fCurrentBuffer)
            statistic.evaluationsCount++

            for (i in fCurrentBuffer.indices) {
                k[0][i] = step * fCurrentBuffer[i]
                yNextBuffer[i] = outY[i] + 1.0 / 3.0 * k[0][i]
            }

            equations(time + 1.0 / 3.0 * step, yNextBuffer, fCurrentBuffer)
            statistic.evaluationsCount++

            for (i in fCurrentBuffer.indices) {
                k[1][i] = step * fCurrentBuffer[i]
                yNextBuffer[i] = outY[i] + 1.0 / 6.0 * k[0][i] + 1.0 / 6.0 * k[1][i]
            }

            equations(time + 1.0 / 3.0 * step, yNextBuffer, fCurrentBuffer)
            statistic.evaluationsCount++

            for (i in fCurrentBuffer.indices) {
                k[2][i] = step * fCurrentBuffer[i]
                yNextBuffer[i] = outY[i] + 1.0 / 8.0 * k[0][i] + 3.0 / 8.0 * k[2][i]
            }

            equations(time + 1.0 / 2.0 * step, yNextBuffer, fCurrentBuffer)
            statistic.evaluationsCount++

            for (i in fCurrentBuffer.indices) {
                k[3][i] = step * fCurrentBuffer[i]
                yNextBuffer[i] = outY[i] + 1.0 / 2.0 * k[0][i] - 3.0 / 2.0 * k[2][i] + 2.0 * k[3][i]
            }

            equations(time + step, yNextBuffer, fCurrentBuffer)
            statistic.evaluationsCount++

            for (i in fCurrentBuffer.indices) {
                k[4][i] = step * fCurrentBuffer[i]
            }

            for (i in vectorBuffer1.indices){
                vectorBuffer1[i] = abs((2.0*k[0][i] - 9.0*k[2][i] + 8.0*k[3][i] - k[4][i]) / 30.0)
            }

            if(!state.isLowStepSizeReached){
                for(i in vectorBuffer1.indices){
                    if(abs(outY[i]) > rVector[i] && vectorBuffer1[i] >= accuracy*abs(outY[i])){
                        step /= 2.0
                        state.isLowStepSizeReached = isLowStepSizeReached(step)
                        statistic.returnsCount++
                        continue@mainLoop
                    }
                }
            }

            for (i in outY.indices){
                outY[i] += 1.0/6.0*k[0][i] + 2.0/3.0*k[3][i] + 1.0/6.0*k[4][i]
            }

            time += step

            statistic.stepsCount++

            executeStepHandlers(time, outY, state, statistic)

            if (!state.isHighStepSizeReached) {
                for (i in vectorBuffer1.indices) {
                    if(vectorBuffer1[i] > accuracy*abs(outY[i]) / 32.0){
                        continue@mainLoop
                    }
                }
                step *= 2.0
                state.isHighStepSizeReached = isHighStepSizeReached(step)
            }
        }
        return statistic
    }
}