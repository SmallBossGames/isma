package ru.nstu.grin.common.converters.dto

import ru.nstu.grin.common.converters.Converter
import ru.nstu.grin.common.dto.DescriptionDTO
import ru.nstu.grin.common.model.view.DescriptionViewModel

object DescriptionDTOConverter :
    Converter<DescriptionViewModel, DescriptionDTO> {
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