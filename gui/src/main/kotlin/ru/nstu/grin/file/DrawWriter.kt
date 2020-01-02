package ru.nstu.grin.file

import ru.nstu.grin.model.Drawable
import ru.nstu.grin.model.drawable.Arrow
import ru.nstu.grin.model.drawable.Description
import ru.nstu.grin.model.drawable.Function
import java.io.File
import java.io.FileOutputStream
import java.io.ObjectOutputStream

/**
 * Write all figures to file
 */
class DrawWriter(private val file: File) {

    /**
     * n -size, figures
     * Descriptions
     * Arrows
     * Functions
     */
    fun write(drawings: List<Drawable>) {
        val descriptions = drawings.filterIsInstance<Description>()
        val arrows = drawings.filterIsInstance<Arrow>()
        val functions = drawings.filterIsInstance<Function>()

        ObjectOutputStream(FileOutputStream(file)).use { oos ->
            oos.writeInt(descriptions.size)
            for (description in descriptions) {
                oos.write(description.serialize())
            }

            oos.writeInt(arrows.size)
            for (arrow in arrows) {
                oos.write(arrow.serialize())
            }

            oos.writeInt(functions.size)
            for (function in functions) {
                oos.write(function.serialize())
            }
        }
    }
}