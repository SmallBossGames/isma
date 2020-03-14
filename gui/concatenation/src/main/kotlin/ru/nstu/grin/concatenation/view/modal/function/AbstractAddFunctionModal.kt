package ru.nstu.grin.concatenation.view.modal.function

import ru.nstu.grin.common.model.DrawSize
import ru.nstu.grin.concatenation.model.ExistDirection
import tornadofx.*

abstract class AbstractAddFunctionModal : Fragment() {
    val drawSize: DrawSize by param()
    val xExistDirections: List<ExistDirection> by param()
    val yExistDirections: List<ExistDirection> by param()
}