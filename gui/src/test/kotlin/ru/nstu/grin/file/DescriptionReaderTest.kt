package ru.nstu.grin.file

import javafx.scene.paint.Color
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import ru.kontur.kinfra.kfixture.annotations.Fixture
import ru.kontur.kinfra.kfixture.api.FixtureGeneratorMeta
import ru.kontur.kinfra.kfixture.resolver.FixtureParameterResolver
import ru.nstu.grin.model.drawable.Description
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream

@ExtendWith(FixtureParameterResolver::class)
@FixtureGeneratorMeta(["ru.nstu.grin"])
internal class DescriptionReaderTest {

    @Test
    fun `should read description`(
        @Fixture description: Description
    ) {
        val baos = ByteArrayOutputStream()
        baos.write(description.serialize())
        val serialized = baos.toByteArray()
        baos.close()
        val bais = ByteArrayInputStream(serialized)
        val ois = ObjectInputStream(bais)
        val descriptionReader = DescriptionReader()
        val deserialized = descriptionReader.deserialize(ois)
        assertEquals(deserialized, description)
    }
}