package ru.nstu.grin.concatenation.function.model

import javafx.scene.paint.Color
import ru.nstu.grin.common.model.Point
import java.util.*

/**
 * @author kostya05983
 */
data class ConcatenationFunction(
    val id: UUID,
    var name: String,
    val points: List<Point>,
    var isHide: Boolean = false,
    var isSelected: Boolean = false,

    var functionColor: Color,

    var lineSize: Double,
    var lineType: LineType,
    var mirrorDetails: MirrorDetails = MirrorDetails(),
    var derivativeDetails: DerivativeDetails? = null,
    var waveletDetails: WaveletDetails? = null,
) : Cloneable {
    public override fun clone(): ConcatenationFunction {
        return ConcatenationFunction(
            id = id,
            name = name,
            points = points.map { it.copy() },
            functionColor = functionColor,
            lineSize = lineSize,
            lineType = lineType,
            mirrorDetails = mirrorDetails.copy(),
            derivativeDetails = derivativeDetails?.copy(),
            waveletDetails = waveletDetails?.copy(),
        )
    }
}
