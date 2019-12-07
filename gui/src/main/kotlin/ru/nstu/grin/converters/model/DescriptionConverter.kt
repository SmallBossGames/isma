package ru.nstu.grin.converters.model

import ru.nstu.grin.converters.Converter
import ru.nstu.grin.dto.DescriptionDTO
import ru.nstu.grin.model.drawable.Description

object DescriptionConverter : Converter<DescriptionDTO, Description> {
    override fun convert(source: DescriptionDTO): Description {
        return Description(
            text = source.text,
            size = source.size,
            x = source.x,
            y = source.y,
            color = source.textColor
        )
    }
}