package ru.nstu.grin.model

/**
 * @author Konstantin Volivach
 */
data class Expression(
    val sequence: List<Calculated>
) : Calculated {
    override fun calculate(x: Double): Double {
        var result = 0.0
        sequence.forEach {
            result += it.calculate(x)
        }
        return result
    }
}