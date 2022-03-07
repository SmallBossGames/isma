package ru.isma.next.math.engine.shared

import kotlin.math.abs
import kotlin.math.sign

fun newtonRaphson(
    guess: Double,
    min: Double,
    max: Double,
    digits: Int,
    maxIter: Int,
    f:(value: Double) -> Pair<Double, Double>
) : Double {
    require(max > min)

    var localMin = min
    var localMax = max
    var localGuess = guess

    var f0: Double
    var f1: Double
    var result = localGuess

    val factor = (1.0 / (1 shl (digits - 1)))
    var delta = Double.MAX_VALUE
    var delta1 = Double.MAX_VALUE

    var maxRangeF = 0.0
    var minRangeF = 0.0
    var count = maxIter

    do {
        val delta2 = delta1
        delta1 = delta

        val functionResult = f(result)
        f0 = functionResult.first
        f1 = functionResult.second

        --count

        if(f0 == 0.0)
            break

        if(f1 == 0.0)
            throw Exception("Derivative cannot be equal zero")
        else
            delta = f0/f1


        if(abs(delta * 2.0) > abs(delta2))
        {
            val shift = if (delta > 0.0)
                (result - localMin) / 2.0
            else
                (result - localMax) / 2.0

            delta = if((result != 0.0) && (abs(shift) > abs(result))) {
                sign(delta) * abs(result) * 1.1
            } else {
                shift
            }

            delta1 = 3 * delta
        }

        localGuess = result
        result -= delta

        if(result <= localMin)
        {
            delta = 0.5 * (localGuess - localMin)
            result = localGuess - delta
            if(result == localMin || result == localMax)
                break
        }
        else if (result >= localMax)
        {
            delta = 0.5 * (localGuess - localMax)
            result = localGuess - delta
            if(result == localMin || result == localMax)
                break
        }

        if (delta > 0)
        {
            localMax = localGuess
            maxRangeF = f0
        }
        else
        {
            localMin = localGuess
            minRangeF = f0
        }

        if(maxRangeF * minRangeF > 0)
            throw Exception("There appears to be no root to be found")

    } while (count != 0 && (abs(result * factor) < abs(delta)))

    return result
}