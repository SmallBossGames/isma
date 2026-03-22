package ru.isma.next.domain.models

interface IEquationIndexProvider {
    fun getDifferentialEquationCount(): Int
    fun getAlgebraicEquationCount(): Int
    fun getDifferentialEquationCode(index: Int): String?
    fun getAlgebraicEquationCode(index: Int): String?
}
