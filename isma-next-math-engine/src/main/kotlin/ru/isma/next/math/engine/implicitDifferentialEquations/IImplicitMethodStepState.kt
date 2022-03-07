package ru.isma.next.math.engine.implicitDifferentialEquations

import ru.isma.next.math.common.Matrix2D

interface IImplicitMethodStepState {
    val jacobiMatrix: Matrix2D
    val isLowStepSizeReached: Boolean
    val isHighStepSizeReached: Boolean
    val freezeJacobiStepsCount: Int
}