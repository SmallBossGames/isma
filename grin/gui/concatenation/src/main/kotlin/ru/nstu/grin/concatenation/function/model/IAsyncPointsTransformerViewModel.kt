package ru.nstu.grin.concatenation.function.model

import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleDoubleProperty
import ru.isma.javafx.extensions.helpers.getValue
import ru.isma.javafx.extensions.helpers.setValue
import ru.nstu.grin.concatenation.function.transform.IAsyncPointsTransformer
import ru.nstu.grin.concatenation.function.transform.LogTransformer
import ru.nstu.grin.concatenation.function.transform.MirrorTransformer

sealed interface IAsyncPointsTransformerViewModel

class LogPointsTransformerViewModel(
    isXLogarithm: Boolean = false,
    xLogBase: Double = 2.0,
    isYLogarithm: Boolean = false,
    yLogBase: Double = 2.0
): IAsyncPointsTransformerViewModel {
    val isXLogarithmProperty = SimpleBooleanProperty(isXLogarithm)
    val isXLogarithm by isXLogarithmProperty

    val isYLogarithmProperty = SimpleBooleanProperty(isYLogarithm)
    val isYLogarithm by isYLogarithmProperty

    val xLogarithmBaseProperty = SimpleDoubleProperty(xLogBase)
    val xLogarithmBase by xLogarithmBaseProperty

    val yLogarithmBaseProperty = SimpleDoubleProperty(yLogBase)
    val yLogarithmBase by yLogarithmBaseProperty
}

class MirrorPointsTransformerViewModel(
    mirrorX: Boolean = false,
    mirrorY: Boolean = false,
): IAsyncPointsTransformerViewModel {
    val mirrorXProperty = SimpleBooleanProperty(mirrorX)
    var mirrorX by mirrorXProperty

    val mirrorYProperty = SimpleBooleanProperty(mirrorY)
    var mirrorY by mirrorYProperty
}

fun IAsyncPointsTransformer.toViewModel(): IAsyncPointsTransformerViewModel = when(this){
    is MirrorTransformer -> toViewModel()
    is LogTransformer -> toViewModel()
    else -> throw NotImplementedError()
}

fun IAsyncPointsTransformerViewModel.toModel(): IAsyncPointsTransformer = when(this){
    is MirrorPointsTransformerViewModel -> toModel()
    is LogPointsTransformerViewModel -> toModel()
}

fun MirrorTransformer.toViewModel() = MirrorPointsTransformerViewModel(mirrorX, mirrorY)
fun MirrorPointsTransformerViewModel.toModel() = MirrorTransformer(mirrorX, mirrorY)

fun LogTransformer.toViewModel() = LogPointsTransformerViewModel(isXLogarithm, xLogBase, isYLogarithm, yLogBase)
fun LogPointsTransformerViewModel.toModel() = LogTransformer(isXLogarithm, xLogarithmBase, isYLogarithm, yLogarithmBase)