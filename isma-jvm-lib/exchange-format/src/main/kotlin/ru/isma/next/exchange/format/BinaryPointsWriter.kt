package ru.isma.next.exchange.format

import java.io.BufferedOutputStream
import java.io.DataOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

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

/**
 * Writes the metadata header followed by all points to an [OutputStream]
 * (e.g. a process's stdout). The header is flushed before the first point so a
 * reader can learn the column layout immediately, and each point is flushed
 * after it is written so a reader can consume result rows (and derive progress
 * from the TIME column) incrementally as the simulation runs. The underlying
 * stream is flushed but never closed, so the caller retains ownership of its
 * lifetime.
 */
fun writeAll(stream: OutputStream, columnNames: List<String>, points: Sequence<DoubleArray>) {
    val dos = DataOutputStream(BufferedOutputStream(stream))

    dos.writeShort(columnNames.size)
    for (name in columnNames) {
        val bytes = name.toByteArray(Charsets.UTF_8)
        dos.writeShort(bytes.size)
        dos.write(bytes)
    }
    dos.flush()

    for (point in points) {
        for (value in point) {
            dos.writeDouble(value)
        }
        dos.flush()
    }
}
