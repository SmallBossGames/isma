package ru.nstu.grin.converters.model

import ru.nstu.grin.converters.Converter
import ru.nstu.grin.dto.common.ArrowDTO
import ru.nstu.grin.model.drawable.Arrow

/**
 * @author Konstantin Volivach
 */
object ArrowConverter : Converter<ArrowDTO, Arrow> {
    override fun convert(source: ArrowDTO): Arrow {
        return Arrow(
            source.color,
            source.x,
            source.y
        )
    }
}