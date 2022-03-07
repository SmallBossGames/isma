package ru.isma.next.math.engine.implicitDifferentialEquations

import ru.isma.next.math.engine.shared.Integrator

abstract class ImplicitIntegrator : Integrator() {
    private val stepHandlers =
        HashSet<(
            time:Double,
            y:DoubleArray,
            state: IImplicitMethodStepState,
            stat: IImplicitMethodStatistic
        ) -> Unit>()

    protected fun executeStepHandlers(
        time:Double,
        y:DoubleArray,
        state: IImplicitMethodStepState,
        stat: IImplicitMethodStatistic
    ) {
        for (handler in stepHandlers)
            handler(time, y, state, stat)
    }

    fun addStepHandler(handler: (
        time:Double,
        y:DoubleArray,
        state: IImplicitMethodStepState,
        stat: IImplicitMethodStatistic
    ) -> Unit) {
        stepHandlers += handler
    }

    fun removeStepHandler(handler: (
        time:Double,
        y:DoubleArray,
        state: IImplicitMethodStepState,
        stat: IImplicitMethodStatistic
    ) -> Unit) {
        stepHandlers -= handler
    }
}