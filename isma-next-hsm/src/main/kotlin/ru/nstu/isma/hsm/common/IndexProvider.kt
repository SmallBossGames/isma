package ru.nstu.isma.hsm.common

/**
 * @author Maria Nasyrova
 * @since 06.10.2015
 */
interface IndexProvider {
    fun getDifferentialArrayCode(code: String?): String
    fun getAlgebraicArrayCode(code: String?): String
    fun getAlgebraicArrayCodeForDifferentialEquation(aeCode: String?): String
}