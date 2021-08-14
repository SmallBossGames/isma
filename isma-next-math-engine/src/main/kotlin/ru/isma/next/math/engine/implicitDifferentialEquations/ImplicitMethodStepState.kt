package ru.isma.next.math.engine.implicitDifferentialEquations

import ru.isma.next.math.engine.shared.Matrix2D

data class ImplicitMethodStepState(
    override val jacobiMatrix: Matrix2D,
    override var isLowStepSizeReached: Boolean,
    override var isHighStepSizeReached: Boolean,
    override var freezeJacobiStepsCount: Int
) : IImplicitMethodStepState