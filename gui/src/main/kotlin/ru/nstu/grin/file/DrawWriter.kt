package ru.nstu.grin.file

import ru.nstu.grin.model.Drawable
import ru.nstu.grin.model.drawable.Arrow
import ru.nstu.grin.model.drawable.Description
import ru.nstu.grin.model.drawable.Function
import ru.nstu.grin.model.drawable.axis.BottomAxis
import ru.nstu.grin.model.drawable.axis.LeftAxis
import ru.nstu.grin.model.drawable.axis.RightAxis
import ru.nstu.grin.model.drawable.axis.TopAxis

/**
 * Write all figures to file
 */
class DrawWriter {


    fun write(drawings: List<Drawable>) {
        for (draw in drawings) {
            when (draw) {
                is BottomAxis -> {

                }
                is LeftAxis -> {

                }
                is RightAxis -> {

                }
                is TopAxis -> {

                }
                is Arrow -> {

                }
                is Description -> {

                }
                is Function -> {

                }
            }
        }
    }
}