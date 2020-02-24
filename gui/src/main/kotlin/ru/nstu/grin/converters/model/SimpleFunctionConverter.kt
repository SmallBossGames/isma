package ru.nstu.grin.converters.model

import ru.nstu.grin.converters.Converter
import ru.nstu.grin.dto.simple.SimpleFunctionDTO
import ru.nstu.grin.model.drawable.SimpleFunction

object SimpleFunctionConverter : Converter<SimpleFunctionDTO, SimpleFunction> {
    override fun convert(source: SimpleFunctionDTO): SimpleFunction {
        return SimpleFunction(
            name = source.name,
            points = source.points,
            color = source.color
        )
    }
}