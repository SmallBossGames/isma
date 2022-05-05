package ru.nstu.grin.concatenation.description.model

import ru.nstu.grin.concatenation.cartesian.model.CartesianSpace

sealed interface IDescriptionModalInitData

data class DescriptionModalForCreate(
    val cartesianSpace: CartesianSpace,
    val xPosition: Double,
    val yPosition: Double,
): IDescriptionModalInitData

data class DescriptionModalForUpdate(
    val cartesianSpace: CartesianSpace,
    val description: Description,
): IDescriptionModalInitData