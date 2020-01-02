package ru.nstu.grin.file

/**
 * Represent an object as array of bytes
 */
interface Writer {
    fun serialize(): ByteArray
}