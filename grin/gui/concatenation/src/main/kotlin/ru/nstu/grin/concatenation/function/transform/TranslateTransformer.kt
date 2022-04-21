package ru.nstu.grin.concatenation.function.transform

data class TranslateTransformer(
    val translateX: Double,
    val translateY: Double,
): IAsyncPointsTransformer {
    override suspend fun transform(x: DoubleArray, y: DoubleArray): Pair<DoubleArray, DoubleArray> {
        for (i in x.indices){
            x[i] = x[i] + translateX
        }

        for (i in y.indices){
            y[i] = y[i] + translateY
        }

        return Pair(x, y)
    }
}