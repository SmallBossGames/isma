package ru.nstu.isma.intg.api.utilities

fun DoubleArray.maxOrThrow(): Double {
    return maxOrNull() ?: throw NoSuchElementException()
}

fun DoubleArray.minOrThrow(): Double {
    return minOrNull() ?: throw NoSuchElementException()
}
