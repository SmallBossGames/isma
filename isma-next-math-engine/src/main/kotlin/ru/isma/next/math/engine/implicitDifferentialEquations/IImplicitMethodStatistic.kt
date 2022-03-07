package ru.isma.next.math.engine.implicitDifferentialEquations

interface IImplicitMethodStatistic {
    val stepsCount: Int
    val evaluationsCount: Int
    val jacobiEvaluationsCount: Int
    val returnsCount: Int
}