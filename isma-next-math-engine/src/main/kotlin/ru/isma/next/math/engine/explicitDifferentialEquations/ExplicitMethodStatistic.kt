package ru.isma.next.math.engine.explicitDifferentialEquations

data class ExplicitMethodStatistic(
    override var stepsCount: Int,
    override var evaluationsCount: Int,
    override var returnsCount: Int
) : IExplicitMethodStatistic