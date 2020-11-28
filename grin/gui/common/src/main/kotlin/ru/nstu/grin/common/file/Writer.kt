package ru.nstu.grin.common.file

import java.io.ObjectOutputStream

/**
 * Represent an object as array of bytes
 */
interface Writer {
    fun serialize(oos: ObjectOutputStream)
}