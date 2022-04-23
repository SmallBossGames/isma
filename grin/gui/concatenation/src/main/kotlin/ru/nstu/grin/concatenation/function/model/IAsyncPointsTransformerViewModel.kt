package ru.nstu.grin.concatenation.function.model

import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleDoubleProperty
import javafx.beans.property.SimpleIntegerProperty
import javafx.beans.property.SimpleObjectProperty
import ru.isma.javafx.extensions.helpers.getValue
import ru.isma.javafx.extensions.helpers.setValue
import ru.nstu.grin.common.model.WaveletDirection
import ru.nstu.grin.common.model.WaveletTransformFun
import ru.nstu.grin.concatenation.function.transform.*
import ru.nstu.grin.concatenation.function.transform.IntegrationMethod

sealed interface IAsyncPointsTransformerViewModel{
    val title: String
}

class TranslateTransformerViewModel(
    translateX: Double = 0.0,
    translateY: Double = 0.0,
): IAsyncPointsTransformerViewModel {
    override val title = Companion.title

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
    override val title = Companion.title

    val isXLogarithmProperty = SimpleBooleanProperty(isXLogarithm)
    val isXLogarithm by isXLogarithmProperty

    val isYLogarithmProperty = SimpleBooleanProperty(isYLogarithm)
    val isYLogarithm by isYLogarithmProperty

    val xLogarithmBaseProperty = SimpleDoubleProperty(xLogBase)
    val xLogarithmBase by xLogarithmBaseProperty

    val yLogarithmBaseProperty = SimpleDoubleProperty(yLogBase)
    val yLogarithmBase by yLogarithmBaseProperty

    companion object {
        const val title = "Logarithm"
    }
}

class MirrorPointsTransformerViewModel(
    mirrorX: Boolean = false,
    mirrorY: Boolean = false,
): IAsyncPointsTransformerViewModel {
    override val title = Companion.title

    val mirrorXProperty = SimpleBooleanProperty(mirrorX)
    var mirrorX by mirrorXProperty

    val mirrorYProperty = SimpleBooleanProperty(mirrorY)
    var mirrorY by mirrorYProperty

    companion object{
        const val title = "Mirror"
    }
}

class WaveletTransformerViewModel(
    waveletTransformFun: WaveletTransformFun = WaveletTransformFun.HAAR1,
    waveletDirection: WaveletDirection = WaveletDirection.BOTH
): IAsyncPointsTransformerViewModel {
    override val title = Companion.title

    val waveletTransformFunProperty = SimpleObjectProperty(waveletTransformFun)
    var waveletTransformFun: WaveletTransformFun by waveletTransformFunProperty

    val waveletDirectionProperty = SimpleObjectProperty(waveletDirection)
    var waveletDirection: WaveletDirection by waveletDirectionProperty

    companion object{
        const val title = "Wavelet"
    }
}

class DerivativeTransformerViewModel(
    degree: Int = 1,
    type: DerivativeType = DerivativeType.BOTH,
    axis: DerivativeAxis = DerivativeAxis.Y_BY_X,
): IAsyncPointsTransformerViewModel {
    override val title = Companion.title

    val degreeProperty = SimpleIntegerProperty(degree)
    var degree by degreeProperty

    val typeProperty = SimpleObjectProperty(type)
    var type: DerivativeType by typeProperty

    val axisProperty = SimpleObjectProperty(axis)
    val axis by axisProperty

    companion object{
        const val title = "Derivative"
    }
}

class IntegratorTransformerViewModel(
    initialValue: Double = 0.0,
    method: IntegrationMethod = IntegrationMethod.TRAPEZE,
    axis: IntegrationAxis = IntegrationAxis.Y_BY_X,
): IAsyncPointsTransformerViewModel {
    override val title = Companion.title

    val initialValueProperty = SimpleDoubleProperty(initialValue)
    var initialValue by initialValueProperty

    val methodProperty = SimpleObjectProperty(method)
    var method by methodProperty

    val axisProperty = SimpleObjectProperty(axis)
    var axis by axisProperty

    companion object {
        const val title = "Integrator"
    }
}

fun IAsyncPointsTransformer.toViewModel(): IAsyncPointsTransformerViewModel = when(this){
    is MirrorTransformer -> toViewModel()
    is LogTransformer -> toViewModel()
    is TranslateTransformer -> toViewModel()
    is WaveletTransformer -> toViewModel()
    is DerivativeTransformer -> toViewModel()
    is IntegratorTransformer -> toViewModel()
    else -> throw NotImplementedError()
}

fun IAsyncPointsTransformerViewModel.toModel(): IAsyncPointsTransformer = when(this){
    is MirrorPointsTransformerViewModel -> toModel()
    is LogPointsTransformerViewModel -> toModel()
    is TranslateTransformerViewModel -> toModel()
    is WaveletTransformerViewModel -> toModel()
    is DerivativeTransformerViewModel -> toModel()
    is IntegratorTransformerViewModel -> toModel()
}

fun MirrorTransformer.toViewModel() = MirrorPointsTransformerViewModel(mirrorX, mirrorY)
fun MirrorPointsTransformerViewModel.toModel() = MirrorTransformer(mirrorX, mirrorY)

fun LogTransformer.toViewModel() = LogPointsTransformerViewModel(isXLogarithm, xLogBase, isYLogarithm, yLogBase)
fun LogPointsTransformerViewModel.toModel() = LogTransformer(isXLogarithm, xLogarithmBase, isYLogarithm, yLogarithmBase)

fun TranslateTransformer.toViewModel() = TranslateTransformerViewModel(translateX, translateY)
fun TranslateTransformerViewModel.toModel() = TranslateTransformer(translateX, translateY)

fun WaveletTransformer.toViewModel() = WaveletTransformerViewModel(waveletTransformFun, waveletDirection)
fun WaveletTransformerViewModel.toModel() = WaveletTransformer(waveletTransformFun, waveletDirection)

fun DerivativeTransformer.toViewModel() = DerivativeTransformerViewModel(degree, type, axis)
fun DerivativeTransformerViewModel.toModel() = DerivativeTransformer(degree, type, axis)

fun IntegratorTransformer.toViewModel() = IntegratorTransformerViewModel(initialValue, method, axis)
fun IntegratorTransformerViewModel.toModel() = IntegratorTransformer(initialValue, method, axis)