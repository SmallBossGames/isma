package ru.nstu.grin.file

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import ru.kontur.kinfra.kfixture.annotations.Fixture
import ru.kontur.kinfra.kfixture.api.FixtureGeneratorMeta
import ru.kontur.kinfra.kfixture.resolver.FixtureParameterResolver
import ru.nstu.grin.model.Drawable
import ru.nstu.grin.model.drawable.Arrow
import ru.nstu.grin.model.drawable.Description
import ru.nstu.grin.model.drawable.ConcatenationFunction
import java.io.File

@ExtendWith(FixtureParameterResolver::class)
@FixtureGeneratorMeta(["ru.nstu.grin"])
internal class DrawReaderTest {

    @Test
    fun `should read all figures`(
        @Fixture description: Description,
        @Fixture arrow: Arrow,
        @Fixture function: ConcatenationFunction
    ) {
        val file = File.createTempFile("test", "draw")
        val writer = DrawWriter(file)
        val drawings = listOf(description, arrow, function) as List<Drawable>
        writer.write(drawings)
        val reader = DrawReader()
        val result = reader.read(file)
        assertEquals(drawings, result)
    }
}