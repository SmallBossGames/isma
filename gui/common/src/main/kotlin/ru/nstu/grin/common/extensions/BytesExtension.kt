package ru.nstu.grin.common.extensions

import javafx.scene.paint.Color
import java.io.ObjectInputStream
import java.nio.ByteBuffer

fun Color.toByteArray(): ByteArray {
    val byteBuffer = ByteBuffer.allocate(24)
    byteBuffer.putDouble(red)
    byteBuffer.putDouble(green)
    byteBuffer.putDouble(blue)
    return byteBuffer.array()
}

fun readColor(ois: ObjectInputStream): Color {
    val red = ois.readDouble()
    val green = ois.readDouble()
    val blue = ois.readDouble()
    return Color.color(red, green, blue)
}