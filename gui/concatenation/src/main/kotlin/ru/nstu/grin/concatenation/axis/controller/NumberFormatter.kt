package ru.nstu.grin.concatenation.axis.controller

import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat

class NumberFormatter {
    fun format(number: Double): String {
        if (number > 0.01 && number < 100 || number > -100 && number < -0.01 || number == 0.0) return number.round(3)
            .toString()
        val decimal = BigDecimal(number)
        val formatter = DecimalFormat("0.0E0")
        formatter.roundingMode = RoundingMode.HALF_DOWN
        formatter.minimumFractionDigits = 2
        return formatter.format(decimal)
    }

    fun formatLogarithmic(number: Double, logBase: Double): String {
        return if (number == 0.0) {
            "1"
        } else {
            "$logBase^${number.round()}"
        }
    }

    private fun Double.round(decimals: Int = 2): Double =
        String.format("%.${decimals}f", this).replace(",", ".").toDouble()
}