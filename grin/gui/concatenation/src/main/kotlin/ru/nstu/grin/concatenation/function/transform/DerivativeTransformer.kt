package ru.nstu.grin.concatenation.function.transform

import ru.nstu.grin.math.Derivatives

enum class DerivativeType { LEFT, RIGHT, BOTH }
enum class DerivativeAxis { X_BY_Y, Y_BY_X }

data class DerivativeTransformer(
    val degree: Int,
    val type: DerivativeType,
    val axis: DerivativeAxis,
): IAsyncPointsTransformer{
    override suspend fun transform(x: DoubleArray, y: DoubleArray): Pair<DoubleArray, DoubleArray> {
        val a = when(axis){
            DerivativeAxis.X_BY_Y -> y
            DerivativeAxis.Y_BY_X -> x
        }

        val b = when(axis){
            DerivativeAxis.X_BY_Y -> x
            DerivativeAxis.Y_BY_X -> y
        }

        return when(type){
            DerivativeType.LEFT -> Derivatives.leftDerivative(a, b, degree)
            DerivativeType.RIGHT -> Derivatives.rightDerivative(a, b, degree)
            DerivativeType.BOTH -> Derivatives.bothDerivatives(a, b, degree)
        }.apply {
            when(axis){
                DerivativeAxis.X_BY_Y -> Pair(second, first)
                DerivativeAxis.Y_BY_X -> Pair(first, second)
            }
        }
    }
}