package ru.nstu.grin.events.common

import ru.nstu.grin.dto.common.ArrowDTO
import ru.nstu.grin.model.CanvasType
import ru.nstu.grin.model.ConcatenationType
import ru.nstu.grin.model.SimpleType
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





