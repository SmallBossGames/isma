package ru.nstu.grin.concatenation.function.model

import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleDoubleProperty
import ru.isma.javafx.extensions.helpers.getValue
import ru.isma.javafx.extensions.helpers.setValue
import ru.nstu.grin.concatenation.function.transform.IAsyncPointsTransformer
import ru.nstu.grin.concatenation.function.transform.LogTransformer
import ru.nstu.grin.concatenation.function.transform.MirrorTransformer
import ru.nstu.grin.concatenation.function.transform.TranslateTransformer

sealed interface IAsyncPointsTransformerViewModel

class TranslateTransformerViewModel(
    translateX: Double = 0.0,
    translateY: Double = 0.0,
): IAsyncPointsTransformerViewModel {
    val translateXProperty = SimpleDoubleProperty(translateX)
    val translateX by translateXProperty

    val translateYProperty = SimpleDoubleProperty(translateY)
    val translateY by translateYProperty

    companion object {
        const val title = "Translate"
    }
}

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
    is TranslateTransformer -> toViewModel()
    else -> throw NotImplementedError()
}

fun IAsyncPointsTransformerViewModel.toModel(): IAsyncPointsTransformer = when(this){
    is MirrorPointsTransformerViewModel -> toModel()
    is LogPointsTransformerViewModel -> toModel()
    is TranslateTransformerViewModel -> toModel()
}

fun MirrorTransformer.toViewModel() = MirrorPointsTransformerViewModel(mirrorX, mirrorY)
fun MirrorPointsTransformerViewModel.toModel() = MirrorTransformer(mirrorX, mirrorY)

fun LogTransformer.toViewModel() = LogPointsTransformerViewModel(isXLogarithm, xLogBase, isYLogarithm, yLogBase)
fun LogPointsTransformerViewModel.toModel() = LogTransformer(isXLogarithm, xLogarithmBase, isYLogarithm, yLogarithmBase)

fun TranslateTransformer.toViewModel() = TranslateTransformerViewModel(translateX, translateY)
fun TranslateTransformerViewModel.toModel() = TranslateTransformer(translateX, translateY)