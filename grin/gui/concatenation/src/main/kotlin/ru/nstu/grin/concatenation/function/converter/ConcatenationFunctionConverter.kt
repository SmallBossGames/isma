package ru.nstu.grin.concatenation.function.converter

import ru.nstu.grin.common.converters.Converter
import ru.nstu.grin.concatenation.function.dto.ConcatenationFunctionDTO
import ru.nstu.grin.concatenation.function.model.ConcatenationFunction

object ConcatenationFunctionConverter : Converter<ConcatenationFunctionDTO, ConcatenationFunction> {
    override fun convert(source: ConcatenationFunctionDTO): ConcatenationFunction {
        return ConcatenationFunction(
            name = source.name,
            xPoints = source.points.map { it.x }.toDoubleArray(),
            yPoints = source.points.map { it.y }.toDoubleArray(),
            functionColor = source.functionColor,
            lineSize = source.lineSize,
            lineType = source.lineType
        )
    }
}