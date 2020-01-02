package ru.nstu.grin.file

import ru.nstu.grin.model.Drawable
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
    fun read(file: File): List<Drawable> {
        val result = mutableListOf<Drawable>()
        FileInputStream(file).use { fis ->
            ObjectInputStream(fis).use { ois ->
                val descriptionSize = ois.readInt()
                for (i in 0 until descriptionSize) {
                    result.add(descriptionReader.deserialize(ois))
                }
                val arrowSize = ois.readInt()
                for (i in 0 until arrowSize) {
                    result.add(arrowReader.deserialize(ois))
                }

                val functionSize = ois.readInt()
                for (i in 0 until functionSize) {
                    result.add(functionReader.deserialize(ois))
                }
            }
        }
        return result
    }
}