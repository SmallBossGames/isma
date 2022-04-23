package ru.nstu.grin.concatenation.canvas.model.project

import javafx.scene.paint.Color
import javafx.scene.text.Font
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.Serializable
import ru.nstu.grin.common.model.Description
import ru.nstu.grin.common.model.Point
import ru.nstu.grin.common.model.WaveletDirection
import ru.nstu.grin.common.model.WaveletTransformFun
import ru.nstu.grin.concatenation.axis.model.*
import ru.nstu.grin.concatenation.cartesian.model.CartesianSpace
import ru.nstu.grin.concatenation.function.model.*
import ru.nstu.grin.concatenation.function.transform.*
import java.util.*

@Serializable
data class ProjectSnapshot(
    val spaces: List<CartesianSpaceSnapshot> = emptyList(),
    val descriptions: List<DescriptionSnapshot> = emptyList(),
)

@Serializable
data class DescriptionSnapshot(
    val id: String,
    var text: String,
    var textSize: Double,
    var x: Double,
    var y: Double,
    var color: ColorSnapshot,
    var font: String,
)

@Serializable
data class CartesianSpaceSnapshot(
    val name: String,
    val functions: List<ConcatenationFunctionSnapshot>,
    val xAxis: ConcatenationAxisSnapshot,
    val yAxis: ConcatenationAxisSnapshot,
    val isShowGrid: Boolean,
)

@Serializable
data class ConcatenationFunctionSnapshot(
    val id: String,
    val name: String,
    val points: List<PointSnapshot>,
    val isHide: Boolean,
    val functionColor: ColorSnapshot,
    val lineSize: Double,
    val lineType: LineType,
    val transformers: List<TransformerSnapshot>
)

@Serializable
data class PointSnapshot(
    val x: Double,
    val y: Double,
)

@Serializable
data class ColorSnapshot(
    val red: Double,
    val green: Double,
    val blue: Double,
    val opacity: Double,
)

@Serializable
data class FontSnapshot(
    val family: String,
    val size: Double,
)

@Serializable
data class ConcatenationAxisSnapshot(
    val name: String,
    val direction: Direction,
    val styleProperties: AxisStylePropertiesSnapshot,
    val scaleProperties: AxisScalePropertiesSnapshot,
)

@Serializable
data class AxisStylePropertiesSnapshot(
    val backgroundColor: ColorSnapshot,
    val marksDistanceType: MarksDistanceType,
    val marksDistance: Double,
    val marksColor: ColorSnapshot,
    val marksFont: FontSnapshot,
    val isVisible: Boolean,
)

@Serializable
data class AxisScalePropertiesSnapshot(
    val scalingType: AxisScalingType = AxisScalingType.LINEAR,
    val scalingLogBase: Double = 10.0,

    val minValue: Double = 0.0,
    val maxValue: Double = 10.0,
)

@Serializable
sealed class TransformerSnapshot

@Serializable
class LogTransformerSnapshot(
    val isXLogarithm: Boolean,
    val xLogBase: Double,
    val isYLogarithm: Boolean,
    val yLogBase: Double
): TransformerSnapshot()

@Serializable
class MirrorTransformerSnapshot(
    val mirrorX: Boolean,
    val mirrorY: Boolean,
): TransformerSnapshot()

@Serializable
class TranslateTransformerSnapshot(
    val translateX: Double,
    val translateY: Double,
): TransformerSnapshot()

@Serializable
data class WaveletTransformerSnapshot(
    val waveletTransformFun: WaveletTransformFun,
    val waveletDirection: WaveletDirection
) : TransformerSnapshot()

@Serializable
data class DerivativeTransformerSnapshot(
    val degree: Int,
    val derivativeType: DerivativeType,
    val derivativeAxis: DerivativeAxis,
) : TransformerSnapshot()

@Serializable
class IntegratorTransformerSnapshot(
    val initialValue: Double,
    val method: IntegrationMethod,
    val axis: IntegrationAxis,
): TransformerSnapshot()

fun DescriptionSnapshot.toModel() =
    Description(
        id = UUID.fromString(id),
        text = text,
        textSize = textSize,
        x = x,
        y = y,
        color = color.toModel(),
        font = font,
    )

fun Description.toSnapshot() =
    DescriptionSnapshot(
        id = id.toString(),
        text = text,
        textSize = textSize,
        x = x,
        y = y,
        color = color.toSnapshot(),
        font = font,
    )

fun AxisStylePropertiesSnapshot.toModel() =
    AxisStyleProperties(
        backgroundColor = backgroundColor.toModel(),
        marksDistanceType = marksDistanceType,
        marksDistance = marksDistance,
        marksColor = marksColor.toModel(),
        marksFont = marksFont.toModel(),
        isVisible = isVisible
    )

fun AxisStyleProperties.toSnapshot() =
    AxisStylePropertiesSnapshot(
        backgroundColor = backgroundColor.toSnapshot(),
        marksDistanceType = marksDistanceType,
        marksDistance = marksDistance,
        marksColor = marksColor.toSnapshot(),
        marksFont = marksFont.toSnapshot(),
        isVisible = isVisible
    )

fun AxisScalePropertiesSnapshot.toModel() =
    AxisScaleProperties(
        scalingType = scalingType,
        scalingLogBase = scalingLogBase,
        minValue = minValue,
        maxValue = maxValue
    )

fun AxisScaleProperties.toSnapshot() =
    AxisScalePropertiesSnapshot(
        scalingType = scalingType,
        scalingLogBase = scalingLogBase,
        minValue = minValue,
        maxValue = maxValue
    )

fun ColorSnapshot.toModel() =
    Color.color(red, green, blue, opacity)

fun Color.toSnapshot() =
    ColorSnapshot(red, green, blue, opacity)

fun FontSnapshot.toModel() =
    Font.font(family, size)

fun Font.toSnapshot() =
    FontSnapshot(family, size)

fun ConcatenationAxisSnapshot.toModel() =
    ConcatenationAxis(
        name = name,
        direction = direction,
        styleProperties = styleProperties.toModel(),
        scaleProperties = scaleProperties.toModel(),
    )

fun ConcatenationAxis.toSnapshot() =
    ConcatenationAxisSnapshot(
        name = name,
        direction = direction,
        styleProperties = styleProperties.toSnapshot(),
        scaleProperties = scaleProperties.toSnapshot(),
    )

fun PointSnapshot.toModel() = Point(x, y)

fun Point.toSnapshot() = PointSnapshot(x, y)

fun ConcatenationFunctionSnapshot.toModel(): ConcatenationFunction {
    val transformersFromSnapshot = transformers.map { it.toModel() }.toTypedArray()

    return ConcatenationFunction(
        id = UUID.fromString(id),
        name = name,
        points = points.map { it.toModel() },
        isHide = isHide,
        functionColor = functionColor.toModel(),
        lineSize = lineSize,
        lineType = lineType,
    ).apply {
        runBlocking {
            updateTransformersTransaction { transformersFromSnapshot }
        }
    }


}

fun ConcatenationFunction.toSnapshot() =
    ConcatenationFunctionSnapshot(
        id = id.toString(),
        name = name,
        points = points.map { it.toSnapshot() },
        isHide = isHide,
        functionColor = functionColor.toSnapshot(),
        lineSize = lineSize,
        lineType = lineType,
        transformers = transformers.map { it.toSnapshot() }
    )

fun CartesianSpaceSnapshot.toModel() =
    CartesianSpace(
        name = name,
        functions = functions.map { it.toModel() }.toMutableList(),
        xAxis = xAxis.toModel(),
        yAxis = yAxis.toModel(),
        isShowGrid = isShowGrid,
    )

fun CartesianSpace.toSnapshot() =
    CartesianSpaceSnapshot(
        name = name,
        functions = functions.map { it.toSnapshot() },
        xAxis = xAxis.toSnapshot(),
        yAxis = yAxis.toSnapshot(),
        isShowGrid = isShowGrid,
    )

fun IAsyncPointsTransformer.toSnapshot(): TransformerSnapshot = when(this){
    is LogTransformer -> LogTransformerSnapshot(isXLogarithm, xLogBase, isYLogarithm, yLogBase)
    is MirrorTransformer -> MirrorTransformerSnapshot(mirrorX, mirrorY)
    is TranslateTransformer -> TranslateTransformerSnapshot(translateX, translateY)
    is WaveletTransformer -> WaveletTransformerSnapshot(waveletTransformFun, waveletDirection)
    is DerivativeTransformer -> DerivativeTransformerSnapshot(degree, type, axis)
    is IntegratorTransformer -> IntegratorTransformerSnapshot(initialValue, method, axis)
    else -> throw NotImplementedError()
}

fun TransformerSnapshot.toModel(): IAsyncPointsTransformer = when(this){
    is LogTransformerSnapshot -> LogTransformer(isXLogarithm, xLogBase, isYLogarithm, yLogBase)
    is MirrorTransformerSnapshot -> MirrorTransformer(mirrorX, mirrorY)
    is TranslateTransformerSnapshot -> TranslateTransformer(translateX, translateY)
    is WaveletTransformerSnapshot -> WaveletTransformer(waveletTransformFun, waveletDirection)
    is DerivativeTransformerSnapshot -> DerivativeTransformer(degree, derivativeType, derivativeAxis)
    is IntegratorTransformerSnapshot -> IntegratorTransformer(initialValue, method, axis)
}