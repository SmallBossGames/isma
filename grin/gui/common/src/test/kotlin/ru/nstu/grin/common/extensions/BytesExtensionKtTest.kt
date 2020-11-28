package ru.nstu.grin.common.extensions

import javafx.scene.paint.Color
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream

internal class BytesExtensionKtTest {
    @Test
    fun `should read color`() {
        val baos = ByteArrayOutputStream()
        val color = Color.ALICEBLUE
        baos.write(color.toByteArray())
        val serialized = baos.toByteArray()
        baos.close()
        val bais = ByteArrayInputStream(serialized)
        val ois = ObjectInputStream(bais)
        val deserialized = readColor(ois)
        ois.close()
        assertEquals(color, deserialized)
    }
}