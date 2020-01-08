package ru.nstu.grin.view.modal.function

import ru.nstu.grin.model.DrawSize
import ru.nstu.grin.model.ExistDirection
import tornadofx.*

abstract class AbstractAddFunctionModal : Fragment() {
    val drawSize: DrawSize by param()
    val xExistDirections: List<ExistDirection> by param()
    val yExistDirections: List<ExistDirection> by param()
}