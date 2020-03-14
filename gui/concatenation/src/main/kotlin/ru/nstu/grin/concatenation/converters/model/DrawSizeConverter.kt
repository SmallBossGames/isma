package ru.nstu.grin.concatenation.converters.model

import ru.nstu.grin.common.converters.Converter
import ru.nstu.grin.concatenation.dto.DrawSizeDTO
import ru.nstu.grin.common.model.DrawSize

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