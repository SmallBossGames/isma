package ru.nstu.grin.events.concatenation

import ru.nstu.grin.dto.common.ArrowDTO
import tornadofx.FXEvent

class ArrowEvent(val arrowDTO: ArrowDTO) : FXEvent()