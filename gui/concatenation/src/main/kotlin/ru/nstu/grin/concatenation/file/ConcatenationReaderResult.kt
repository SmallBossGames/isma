package ru.nstu.grin.concatenation.file

import ru.nstu.grin.common.model.Arrow
import ru.nstu.grin.common.model.Description
import ru.nstu.grin.concatenation.function.model.ConcatenationFunction

data class ConcatenationReaderResult(
    val functions: List<ConcatenationFunction>,
    val arrows: List<Arrow>,
    val descriptions: List<Description>
)