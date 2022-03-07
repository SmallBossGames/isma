package ru.nstu.grin.concatenation.axis.model

data class UpdateLogarithmicTypeChangeSet(
    val logarithmBase: Double,
    val isOnlyIntegerPow: Boolean,
    val integerStep: Int,
)