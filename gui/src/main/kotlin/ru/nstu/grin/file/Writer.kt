package ru.nstu.grin.file

import java.io.ObjectOutputStream

/**
 * Represent an object as array of bytes
 */
interface Writer {
    fun serialize(oos: ObjectOutputStream)
}