package ru.nstu.grin.converters.model

import ru.nstu.grin.converters.Converter
import ru.nstu.grin.dto.DrawSizeDTO
import ru.nstu.grin.model.DrawSize

object DrawSizeConverter : Converter<DrawSizeDTO, DrawSize> {
    override fun convert(source: DrawSizeDTO): DrawSize {
        return DrawSize(
            minX = source.minX,
            maxX = source.maxX,
            minY = source.minY,
            maxY = source.maxY
        )
    }
}