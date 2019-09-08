package ru.nstu.grin.converters

import ru.nstu.grin.dto.ArrowDTO
import ru.nstu.grin.model.Arrow

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