package ru.isma.next.math.engine.explicitDifferentialEquations

interface IExplicitMethodStepState {
    val isLowStepSizeReached:Boolean
    val isHighStepSizeReached: Boolean
}