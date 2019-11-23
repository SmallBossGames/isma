package ru.nstu.grin.converters.dto

import ru.nstu.grin.converters.Converter
import ru.nstu.grin.dto.ArrowDTO
import ru.nstu.grin.model.ArrowViewModel

object ArrowDTOConverter : Converter<ArrowViewModel, ArrowDTO> {
    override fun convert(source: ArrowViewModel): ArrowDTO {
        return ArrowDTO(
            color = source.arrowColor,
            x = source.x,
            y = source.y
        )
    }
}