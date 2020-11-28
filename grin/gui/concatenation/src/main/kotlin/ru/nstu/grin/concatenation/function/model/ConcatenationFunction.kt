package ru.nstu.grin.concatenation.function.model

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import javafx.scene.paint.Color
import javafx.scene.shape.Line
import javafx.scene.shape.Shape
import ru.nstu.grin.common.model.Point
import ru.nstu.grin.concatenation.file.json.ColorDeserializer
import ru.nstu.grin.concatenation.file.json.ColorSerializer
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

    @field:JsonDeserialize(using = ColorDeserializer::class)
    @field:JsonSerialize(using = ColorSerializer::class)
    var functionColor: Color,

    var lineSize: Double,
    var lineType: LineType,
    val details: MutableList<ConcatenationFunctionDetails> = mutableListOf(MirrorDetails())
) : Cloneable {
    @JsonIgnore
    public override fun clone(): ConcatenationFunction {
        return ConcatenationFunction(
            id = id,
            name = name,
            points = points.map { it.clone() as Point },
            functionColor = functionColor,
            lineSize = lineSize,
            lineType = lineType,
            details = details.map {
                when (it) {
                    is MirrorDetails -> it.copy()
                    is DerivativeDetails -> it.copy()
                    is WaveletDetails -> it.copy()
                }
            }.toMutableList()
        )
    }

    @JsonIgnore
    fun replaceMirrorDetails(details: MirrorDetails) {
        this.details.removeIf { it is MirrorDetails }
        this.details.add(details)
    }

    @JsonIgnore
    fun getMirrorDetails() = details.filterIsInstance<MirrorDetails>().first()

    @JsonIgnore
    fun getDerivativeDetails() = details.filterIsInstance<DerivativeDetails>().firstOrNull()

    @JsonIgnore
    fun getWaveletDetails() = details.filterIsInstance<WaveletDetails>().firstOrNull()

    @JsonIgnore
    fun removeDerivativeDetails() = details.removeIf { it is DerivativeDetails }

    @JsonIgnore
    fun removeWaveletDetails() = details.removeIf { it is WaveletDetails }

    @JsonIgnore
    fun getShape(): Shape {
        return Line(0.0, 10.0, 0.0, 20.0)
    }
}
