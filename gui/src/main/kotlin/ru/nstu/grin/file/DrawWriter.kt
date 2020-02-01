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
        FileOutputStream(file).use {
            ObjectOutputStream(it).use { oos ->
                oos.writeInt(descriptions.size)
                oos.writeInt(arrows.size)
                oos.writeInt(functions.size)

                for (description in descriptions) {
                    description.serialize(oos)
                }

                for (arrow in arrows) {
                    arrow.serialize(oos)
                }

                for (function in functions) {
                    function.serialize(oos)
                }
            }
        }
    }
}