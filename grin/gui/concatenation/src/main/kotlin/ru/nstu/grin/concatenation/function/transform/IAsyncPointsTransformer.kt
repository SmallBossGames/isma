package ru.nstu.grin.concatenation.function.transform

interface IAsyncPointsTransformer {
    suspend fun transform(x: DoubleArray, y: DoubleArray): Pair<DoubleArray, DoubleArray>
}