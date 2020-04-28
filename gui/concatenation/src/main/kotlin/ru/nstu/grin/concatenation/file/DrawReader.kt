package ru.nstu.grin.concatenation.file

import ru.nstu.grin.common.model.Arrow
import ru.nstu.grin.common.model.Description
import ru.nstu.grin.concatenation.function.model.ConcatenationFunction
import java.io.File
import java.io.FileInputStream
import java.io.ObjectInputStream

/**
 * Read all canvas elements
 */
class DrawReader {
    private val descriptionReader = DescriptionReader()
    private val arrowReader = ArrowReader()
    private val functionReader = FunctionReader()

    /**
     * n -size, figures
     * Descriptions
     * Arrows
     * Functions
     */
    fun read(file: File): ConcatenationReaderResult {
        return FileInputStream(file).use { fis ->
            ObjectInputStream(fis).use { ois ->
                val descriptions = mutableListOf<Description>()
                val arrows = mutableListOf<Arrow>()
                val functions = mutableListOf<ConcatenationFunction>()
                val descriptionSize = ois.readInt()
                val arrowSize = ois.readInt()
                val functionSize = ois.readInt()

                for (i in 0 until descriptionSize) {
                    descriptions.add(descriptionReader.deserialize(ois))
                }
                for (i in 0 until arrowSize) {
                    arrows.add(arrowReader.deserialize(ois))
                }
                for (i in 0 until functionSize) {
                    functions.add(functionReader.deserialize(ois))
                }
                ConcatenationReaderResult(
                    descriptions = descriptions,
                    arrows = arrows,
                    functions = functions
                )
            }
        }
    }
}