package ru.nstu.grin.common.converters.model

import ru.nstu.grin.common.converters.Converter
import ru.nstu.grin.common.dto.DescriptionDTO
import ru.nstu.grin.common.model.Description

object DescriptionConverter : Converter<DescriptionDTO, Description> {
    override fun convert(source: DescriptionDTO): Description {
        return Description(
            id = source.id,
            text = source.text,
            size = source.size,
            x = source.x,
            y = source.y,
            color = source.textColor,
            font = source.font
        )
    }
}