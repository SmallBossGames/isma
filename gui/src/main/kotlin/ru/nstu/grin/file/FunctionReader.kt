package ru.nstu.grin.file

import javafx.scene.paint.Color
import ru.nstu.grin.Direction
import ru.nstu.grin.model.FormType
import ru.nstu.grin.model.Point
import java.io.File
import java.io.FileInputStream
import java.io.InputStreamReader

/**
 * @author Konstantin Volivach
 */
class FunctionReader : Reader {

    /**
     * Input format
     * 22.1,23.2|22.1,123
     */
    override fun read(file: File): FormType {
        val pointArray = mutableListOf<Point>()
        InputStreamReader(FileInputStream(file)).useLines {
            for (line in it) {
                val split = line.split('|')
                for (point in split) {
                    val first = point.substring(0, point.indexOf(','))
                    val second = point.substring(point.indexOf(','), point.length)
                    pointArray.add(Point(first.toDouble(), second.toDouble()))
                }
            }
        }
        return ru.nstu.grin.model.Function(
            pointArray = pointArray,
            xDirection = Direction.BOTTOM,
            yDirection = Direction.LEFT,
            minDelta = 0.1,
            delta = 0.5,
            functionColor = Color.BLACK,
            xAxisColor = Color.WHITE,
            yAxisColor = Color.WHITE
        )
    }
}