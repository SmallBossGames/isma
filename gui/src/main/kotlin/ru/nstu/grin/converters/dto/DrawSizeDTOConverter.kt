package ru.nstu.grin.converters.dto

import ru.nstu.grin.converters.Converter
import ru.nstu.grin.dto.DrawSizeDTO
import ru.nstu.grin.model.DrawSize

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