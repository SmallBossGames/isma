package ru.isma.next.math.engine.shared

data class ImplicitIntegratorResult(
    val stepsCount: Int,
    val averageStep: Double,
    val evaluationsCount: Int,
    val jacobiEvaluationsCount: Int,
    val returnsCount: Int)