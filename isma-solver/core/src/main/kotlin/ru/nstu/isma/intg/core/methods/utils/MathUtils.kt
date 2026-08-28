package ru.nstu.isma.intg.core.methods.utils

fun DoubleArray.maxOrThrow(): Double {
    return maxOrNull() ?: throw NoSuchElementException()
}

fun DoubleArray.minOrThrow(): Double {
    return minOrNull() ?: throw NoSuchElementException()
}
