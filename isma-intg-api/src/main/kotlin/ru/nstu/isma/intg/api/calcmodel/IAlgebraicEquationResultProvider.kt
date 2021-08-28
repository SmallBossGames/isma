package ru.nstu.isma.intg.api.calcmodel

/**
 * @author Maria Nasyrova
 * @since 04.10.2015
 */
interface IAlgebraicEquationResultProvider {
    fun getValue(index: Int): Double
}