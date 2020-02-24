package ru.nstu.grin.file

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import ru.kontur.kinfra.kfixture.annotations.Fixture
import ru.kontur.kinfra.kfixture.api.FixtureGeneratorMeta
import ru.kontur.kinfra.kfixture.resolver.FixtureParameterResolver
import ru.nstu.grin.model.drawable.Arrow
import ru.nstu.grin.model.drawable.Description
import ru.nstu.grin.model.drawable.ConcatenationFunction
import java.io.File

@ExtendWith(FixtureParameterResolver::class)
@FixtureGeneratorMeta(["ru.nstu.grin"])
internal class DrawWriterTest {

    @Test
    fun `should write all types of shapes`(
        @Fixture description: Description,
        @Fixture arrow: Arrow,
        @Fixture function: ConcatenationFunction
    ) {
        val file = File.createTempFile("test", "draw")
        val writer = DrawWriter(file)
        writer.write(listOf(description, arrow, function))
    }
}