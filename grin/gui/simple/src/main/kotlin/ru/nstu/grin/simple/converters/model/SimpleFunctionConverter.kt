package ru.nstu.grin.simple.converters.model

import ru.nstu.grin.common.converters.Converter
import ru.nstu.grin.simple.dto.SimpleFunctionDTO
import ru.nstu.grin.simple.model.SimpleFunction

object SimpleFunctionConverter : Converter<SimpleFunctionDTO, SimpleFunction> {
    override fun convert(source: SimpleFunctionDTO): SimpleFunction {
        return SimpleFunction(
            name = source.name,
            points = source.points,
            color = source.color
        )
    }
}