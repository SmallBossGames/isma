package ru.isma.next.exchange.format

import java.io.BufferedInputStream
import java.io.DataInputStream
import java.io.EOFException
import java.io.File
import java.io.FileInputStream

fun readAllPointsSequence(file: File): Sequence<DoubleArray> = sequence {
    val fis = FileInputStream(file)
    val bis = BufferedInputStream(fis)
    val dis = DataInputStream(bis)

    val columnCount = dis.readShort().toInt() and 0xFFFF
    repeat(columnCount) {
        val len = dis.readShort().toInt() and 0xFFFF
        dis.skipBytes(len)
    }

    try {
        while (true) {
            val row = DoubleArray(columnCount)
            for (i in 0 until columnCount) {
                row[i] = dis.readDouble()
            }
            yield(row)
        }
    } catch (_: EOFException) {
    }
}
