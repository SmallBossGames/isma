package ru.nstu.grin.concatenation.canvas.model

import ru.nstu.grin.common.model.Arrow
import ru.nstu.grin.common.model.Description

data class InitCanvasData(
    val cartesianSpaces: List<CartesianSpace>,
    val arrows: List<Arrow>,
    val descriptions: List<Description>
)