package ru.nstu.grin.concatenation.canvas.model.project

import javafx.scene.paint.Color
import kotlinx.serialization.Serializable
import ru.nstu.grin.common.model.Description
import ru.nstu.grin.common.model.Point
import ru.nstu.grin.common.model.WaveletDirection
import ru.nstu.grin.common.model.WaveletTransformFun
import ru.nstu.grin.concatenation.axis.model.AxisMarkType
import ru.nstu.grin.concatenation.axis.model.AxisSettings
import ru.nstu.grin.concatenation.axis.model.ConcatenationAxis
import ru.nstu.grin.concatenation.axis.model.Direction
import ru.nstu.grin.concatenation.cartesian.model.CartesianSpace
import ru.nstu.grin.concatenation.function.model.*
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
    val isSelected: Boolean,

    val functionColor: ColorSnapshot,

    val lineSize: Double,
    val lineType: LineType,
    var mirrorDetails: MirrorDetailsSnapshot,
    var derivativeDetails: DerivativeDetailsSnapshot?,
    var waveletDetails: WaveletDetailsSnapshot?,
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
)

@Serializable
sealed class ConcatenationFunctionDetailsSnapshot

@Serializable
data class MirrorDetailsSnapshot(
    val isMirrorX: Boolean,
    val isMirrorY: Boolean,
) : ConcatenationFunctionDetailsSnapshot()

@Serializable
data class DerivativeDetailsSnapshot(
    val degree: Int,
    val type: DerivativeType
) : ConcatenationFunctionDetailsSnapshot()

@Serializable
data class WaveletDetailsSnapshot(
    val waveletTransformFun: WaveletTransformFun,
    val waveletDirection: WaveletDirection
) : ConcatenationFunctionDetailsSnapshot()

@Serializable
data class ConcatenationAxisSnapshot(
    val name: String,
    val order: Int,
    val direction: Direction,
    val backGroundColor: ColorSnapshot,
    val fontColor: ColorSnapshot,
    val distanceBetweenMarks: Double,
    val textSize: Double,
    val font: String,
    val isHide: Boolean,
    val axisMarkType: AxisMarkType,
    val settings: AxisSettingsSnapshot,
)

@Serializable
data class AxisSettingsSnapshot(
    val isOnlyIntegerPow: Boolean,
    val integerStep: Int,

    val isLogarithmic: Boolean,
    val logarithmBase: Double,

    val min: Double,
    val max: Double,
)

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

fun AxisSettingsSnapshot.toModel() =
    AxisSettings(
        isOnlyIntegerPow = isOnlyIntegerPow,
        integerStep = integerStep,
        isLogarithmic = isLogarithmic,
        logarithmBase = logarithmBase,
        min = min,
        max = max,
    )

fun AxisSettings.toSnapshot() =
    AxisSettingsSnapshot(
        isOnlyIntegerPow = isOnlyIntegerPow,
        integerStep = integerStep,
        isLogarithmic = isLogarithmic,
        logarithmBase = logarithmBase,
        min = min,
        max = max,
    )

fun ColorSnapshot.toModel() =
    Color.color(red, green, blue)

fun Color.toSnapshot() =
    ColorSnapshot(red, green, blue)

fun ConcatenationAxisSnapshot.toModel() =
    ConcatenationAxis(
        name = name,
        order = order,
        direction = direction,
        backGroundColor = backGroundColor.toModel(),
        fontColor = fontColor.toModel(),
        distanceBetweenMarks = distanceBetweenMarks,
        textSize = textSize,
        font = font,
        isHide = isHide,
        axisMarkType = axisMarkType,
        settings = settings.toModel()
    )

fun ConcatenationAxis.toSnapshot() =
    ConcatenationAxisSnapshot(
        name = name,
        order = order,
        direction = direction,
        backGroundColor = backGroundColor.toSnapshot(),
        fontColor = fontColor.toSnapshot(),
        distanceBetweenMarks = distanceBetweenMarks,
        textSize = textSize,
        font = font,
        isHide = isHide,
        axisMarkType = axisMarkType,
        settings = settings.toSnapshot()
    )

fun PointSnapshot.toModel() = Point(x, y)

fun Point.toSnapshot() = PointSnapshot(x, y)

fun DerivativeDetailsSnapshot.toModel() = DerivativeDetails(degree, type)

fun DerivativeDetails.toSnapshot() = DerivativeDetailsSnapshot(degree, type)

fun MirrorDetailsSnapshot.toModel() = MirrorDetails(isMirrorX, isMirrorY)

fun MirrorDetails.toSnapshot() = MirrorDetailsSnapshot(isMirrorX, isMirrorY)

fun WaveletDetailsSnapshot.toModel() = WaveletDetails(waveletTransformFun, waveletDirection)

fun WaveletDetails.toSnapshot() = WaveletDetailsSnapshot(waveletTransformFun, waveletDirection)

fun ConcatenationFunctionSnapshot.toModel() =
    ConcatenationFunction(
        id = UUID.fromString(id),
        name = name,
        points = points.map { it.toModel() },
        isHide = isHide,
        isSelected = isSelected,
        functionColor = functionColor.toModel(),
        lineSize = lineSize,
        lineType = lineType,
        mirrorDetails = mirrorDetails.toModel(),
        derivativeDetails = derivativeDetails?.toModel(),
        waveletDetails = waveletDetails?.toModel(),
    )

fun ConcatenationFunction.toSnapshot() =
    ConcatenationFunctionSnapshot(
        id = id.toString(),
        name = name,
        points = points.map { it.toSnapshot() },
        isHide = isHide,
        isSelected = isSelected,
        functionColor = functionColor.toSnapshot(),
        lineSize = lineSize,
        lineType = lineType,
        mirrorDetails = mirrorDetails.toSnapshot(),
        derivativeDetails = derivativeDetails?.toSnapshot(),
        waveletDetails = waveletDetails?.toSnapshot(),
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
