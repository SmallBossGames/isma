package ru.isma.next.math.engine.explicitDifferentialEquations

data class ExplicitMethodStepState(
    override var isLowStepSizeReached: Boolean,
    override var isHighStepSizeReached: Boolean
) : IExplicitMethodStepState