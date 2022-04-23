package ru.nstu.grin.concatenation.function.transform

import kotlinx.coroutines.coroutineScope
import ru.nstu.grin.math.Integration

enum class IntegrationMethod { TRAPEZE }
enum class IntegrationAxis { X_BY_Y, Y_BY_X }


class IntegratorTransformer(
    val initialValue: Double,
    val method: IntegrationMethod,
    val axis: IntegrationAxis,
): IAsyncPointsTransformer {
    override suspend fun transform(x: DoubleArray, y: DoubleArray): Pair<DoubleArray, DoubleArray> {
        return coroutineScope {
            val a = when(axis){
                IntegrationAxis.X_BY_Y -> y
                IntegrationAxis.Y_BY_X -> x
            }

            val b = when(axis){
                IntegrationAxis.X_BY_Y -> x
                IntegrationAxis.Y_BY_X -> y
            }

            when(method){
                IntegrationMethod.TRAPEZE -> Integration.trapeze(a, b, initialValue)
            }.apply {
                when(axis){
                    IntegrationAxis.X_BY_Y -> Pair(second, first)
                    IntegrationAxis.Y_BY_X -> Pair(first, second)
                }
            }
        }
    }
}