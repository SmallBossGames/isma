package ru.nstu.grin.concatenation.function.transform

import kotlinx.coroutines.coroutineScope

data class MirrorTransformer(
    val mirrorX: Boolean = false,
    val mirrorY: Boolean = false,
): IAsyncPointsTransformer {
    override suspend fun transform(x: DoubleArray, y: DoubleArray) = coroutineScope {
        if(mirrorX){
            for (i in x.indices){
                x[i] = -x[i]
            }
        }

        if(mirrorY){
            for (i in y.indices){
                y[i] = -y[i]
            }
        }
    }

}