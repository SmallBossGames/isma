package ru.nstu.grin.concatenation.file

import ru.nstu.grin.common.model.Arrow
import ru.nstu.grin.common.model.Description
import ru.nstu.grin.concatenation.function.model.ConcatenationFunction
import java.io.File
import java.io.FileOutputStream
import java.io.ObjectOutputStream

/**
 * Write all figures to file
 */
class DrawWriter(private val file: File) {

//    /**
//     * n -size, figures
//     * Descriptions
//     * Arrows
//     * Functions
//     */
//    fun write(drawings: List<Drawable>) {
//        val descriptions = drawings.filterIsInstance<Description>()
//        val arrows = drawings.filterIsInstance<Arrow>()
//        val functions = drawings.filterIsInstance<ConcatenationFunction>()
//        FileOutputStream(file).use {
//            ObjectOutputStream(it).use { oos ->
//                oos.writeInt(descriptions.size)
//                oos.writeInt(arrows.size)
//                oos.writeInt(functions.size)
//
//                for (description in descriptions) {
//                    description.serialize(oos)
//                }
//
//                for (arrow in arrows) {
//                    arrow.serialize(oos)
//                }
//
//                for (function in functions) {
//                    function.serialize(oos)
//                }
//            }
//        }
//    }
}