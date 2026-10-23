package ru.isma.next.exchange.format

import java.io.BufferedInputStream
import java.io.DataInputStream
import java.io.File
import java.io.FileInputStream

fun readMetadata(file: File): BinaryMetadata {
    val dis = DataInputStream(BufferedInputStream(FileInputStream(file)))
    val columnCount = dis.readShort().toInt() and 0xFFFF

    val columnNames = (0 until columnCount).map {
        val len = dis.readShort().toInt() and 0xFFFF
        val bytes = ByteArray(len)
        dis.readFully(bytes)
        String(bytes, Charsets.UTF_8)
    }

    return BinaryMetadata(columnNames)
}

data class BinaryMetadata(
    val columnNames: List<String>,
)
