package ru.nstu.grin.concatenation.file

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import ru.kontur.kinfra.kfixture.annotations.Fixture
import ru.kontur.kinfra.kfixture.api.FixtureGeneratorMeta
import ru.kontur.kinfra.kfixture.resolver.FixtureParameterResolver
import ru.nstu.grin.common.model.Arrow
import ru.nstu.grin.common.model.Description
import ru.nstu.grin.concatenation.function.model.ConcatenationFunction
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