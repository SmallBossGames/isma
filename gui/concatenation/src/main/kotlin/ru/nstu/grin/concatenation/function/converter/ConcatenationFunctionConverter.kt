package ru.nstu.grin.concatenation.function.converter

import ru.nstu.grin.common.converters.Converter
import ru.nstu.grin.concatenation.function.dto.ConcatenationFunctionDTO
import ru.nstu.grin.concatenation.function.model.ConcatenationFunction

object ConcatenationFunctionConverter : Converter<ConcatenationFunctionDTO, ConcatenationFunction> {
    override fun convert(source: ConcatenationFunctionDTO): ConcatenationFunction {
        return ConcatenationFunction(
            id = source.id,
            name = source.name,
            points = source.points,
            functionColor = source.functionColor
        )
    }
}