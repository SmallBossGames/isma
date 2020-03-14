package ru.nstu.grin.common.converters.dto

import ru.nstu.grin.common.converters.Converter
import ru.nstu.grin.common.dto.ArrowDTO
import ru.nstu.grin.common.model.view.ArrowViewModel

object ArrowDTOConverter : Converter<ArrowViewModel, ArrowDTO> {
    override fun convert(source: ArrowViewModel): ArrowDTO {
        return ArrowDTO(
            color = source.arrowColor,
            x = source.x,
            y = source.y
        )
    }
}