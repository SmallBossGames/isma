package ru.isma.next.exchange.format

import java.io.BufferedOutputStream
import java.io.DataOutputStream
import java.io.File
import java.io.FileOutputStream

fun writeMetadata(file: File, columnNames: List<String>) {
    val dos = DataOutputStream(BufferedOutputStream(FileOutputStream(file)))

    dos.writeShort(columnNames.size)

    for (name in columnNames) {
        val bytes = name.toByteArray(Charsets.UTF_8)
        dos.writeShort(bytes.size)
        dos.write(bytes)
    }

    dos.flush()
    dos.close()
}

fun writePoints(file: File, points: Sequence<DoubleArray>) {
    val dos = DataOutputStream(BufferedOutputStream(FileOutputStream(file, true)))

    for (point in points) {
        for (value in point) {
            dos.writeDouble(value)
        }
    }

    dos.flush()
    dos.close()
}

fun writeAll(file: File, columnNames: List<String>, points: Sequence<DoubleArray>) {
    writeMetadata(file, columnNames)
    writePoints(file, points)
}
