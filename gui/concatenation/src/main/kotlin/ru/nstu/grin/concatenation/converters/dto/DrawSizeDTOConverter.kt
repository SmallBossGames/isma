package ru.nstu.grin.concatenation.converters.dto

import ru.nstu.grin.common.converters.Converter
import ru.nstu.grin.common.model.DrawSize
import ru.nstu.grin.concatenation.dto.DrawSizeDTO

object DrawSizeDTOConverter : Converter<DrawSize, DrawSizeDTO> {
    override fun convert(source: DrawSize): DrawSizeDTO {
        return DrawSizeDTO(
            minX = source.minX,
            minY = source.minY,
            maxX = source.maxX,
            maxY = source.maxY
        )
    }
}