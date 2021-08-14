package ru.isma.next.math.engine.explicitDifferentialEquations

interface IExplicitMethodStatistic {
    val stepsCount: Int
    val evaluationsCount: Int
    val returnsCount: Int
}