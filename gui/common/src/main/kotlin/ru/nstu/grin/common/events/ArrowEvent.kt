package ru.nstu.grin.common.events

import ru.nstu.grin.common.dto.ArrowDTO
import tornadofx.FXEvent

sealed class ArrowEvent(
    val arrow: ArrowDTO
) : FXEvent()

class SimpleArrowEvent(arrow: ArrowDTO) : ArrowEvent(
    arrow = arrow
)

class ConcatenationArrowEvent(arrow: ArrowDTO) : ArrowEvent(
    arrow = arrow
)





