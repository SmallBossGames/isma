package ru.nstu.grin.concatenation.file.model

import ru.nstu.grin.common.model.Arrow
import ru.nstu.grin.common.model.Description
import ru.nstu.grin.concatenation.cartesian.model.CartesianSpace

data class SavedCanvas(
    val cartesians: List<CartesianSpace>,
    val arrows: List<Arrow>,
    val descriptions: List<Description>
)