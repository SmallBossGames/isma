package ru.nstu.grin.extensions

import javafx.scene.paint.Color
import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

fun Color.toByteArray(): ByteArray {
    return ByteArrayOutputStream().use { baos ->
        ObjectOutputStream(baos).use {
            it.writeDouble(red)
            it.writeDouble(green)
            it.writeDouble(blue)
        }
        baos
    }.toByteArray()
}

fun readColor(ois: ObjectInputStream): Color {
    val red = ois.readDouble()
    val green = ois.readDouble()
    val blue = ois.readDouble()
    return Color.color(red, green, blue)
}