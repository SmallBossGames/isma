package ru.nstu.grin.controller.events

import ru.nstu.grin.dto.ArrowDTO
import tornadofx.FXEvent

class AddArrowEvent(val arrowDTO: ArrowDTO) : FXEvent()