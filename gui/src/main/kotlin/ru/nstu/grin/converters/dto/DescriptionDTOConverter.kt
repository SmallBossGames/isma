package ru.nstu.grin.converters.dto

import ru.nstu.grin.converters.Converter
import ru.nstu.grin.dto.common.DescriptionDTO
import ru.nstu.grin.model.view.DescriptionViewModel

object DescriptionDTOConverter : Converter<DescriptionViewModel, DescriptionDTO> {
    override fun convert(source: DescriptionViewModel): DescriptionDTO {
        return DescriptionDTO(
            x = source.x,
            y = source.y,
            text = source.text,
            size = source.size,
            textColor = source.textColor
        )
    }
}