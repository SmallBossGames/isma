package ru.nstu.grin.common.converters.model

import ru.nstu.grin.common.converters.Converter
import ru.nstu.grin.common.dto.ArrowDTO
import ru.nstu.grin.common.model.Arrow

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