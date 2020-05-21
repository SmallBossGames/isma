package ru.nstu.grin.concatenation.canvas

import javafx.scene.paint.Color
import ru.nstu.grin.common.common.SettingsProvider
import ru.nstu.grin.common.model.Point
import ru.nstu.grin.common.utils.ColorUtils
import ru.nstu.grin.concatenation.axis.dto.ConcatenationAxisDTO
import ru.nstu.grin.concatenation.axis.model.Direction
import ru.nstu.grin.concatenation.canvas.dto.CartesianSpaceDTO
import ru.nstu.grin.concatenation.canvas.model.ExistDirection
import ru.nstu.grin.concatenation.function.dto.ConcatenationFunctionDTO
import ru.nstu.grin.concatenation.function.model.LineType
import java.awt.Font
import java.util.*
import kotlin.math.cos
import kotlin.math.sin

object GenerateUtils {
    fun generateCircle(radius: Double): List<Point> {
        val result = mutableListOf<Point>()
        for (i in 0 until 360) {
            val x = radius * cos(i.toDouble())
            val y = radius * sin(i.toDouble())
            result.add(Point(x, y))
        }
        return result
    }

    fun generateSin(): List<Point> {
        val result = mutableListOf<Point>()
        var i = -100.0
        while (i < 100) {
            val y = sin(i.toDouble())
            result.add(Point(i.toDouble(), y))
            i += 0.01
        }
        return result
    }

    fun generateTwoCartesianSpaces(): Pair<CartesianSpaceDTO, CartesianSpaceDTO> {
        val function = ConcatenationFunctionDTO(
            id = UUID.randomUUID(),
            name = "Test",
            points = generateCircle(radius = 2.0),
            functionColor = Color.BLACK,
            lineSize = 20.0,
            lineType = LineType.POLYNOM
        )

        val function2 = ConcatenationFunctionDTO(
            id = UUID.randomUUID(),
            name = "Test",
            points = generateSin(),
            functionColor = Color.BLACK,
            lineSize = 1.0,
            lineType = LineType.POLYNOM
        )

        val cartesianSpace = CartesianSpaceDTO(
            id = UUID.randomUUID(),
            name = "пространство 1",
            functions = listOf(function),
            xAxis = ConcatenationAxisDTO(
                id = UUID.randomUUID(),
                name = "Test",
                backGroundColor = ColorUtils.getRandomColor(),
                delimeterColor = Color.BLACK,
                direction = ExistDirection(
                    Direction.BOTTOM,
                    null
                ),
                zeroPoint = SettingsProvider.getCanvasWidth() / 2,
                textSize = 10.0,
                distanceBetweenMarks = 40.0,
                font = Font.SERIF
            ),
            yAxis = ConcatenationAxisDTO(
                id = UUID.randomUUID(),
                name = "Test",
                backGroundColor = ColorUtils.getRandomColor(),
                delimeterColor = Color.BLACK,
                direction = ExistDirection(
                    Direction.LEFT,
                    null
                ),
                zeroPoint = SettingsProvider.getCanvasHeight() / 2,
                textSize = 10.0,
                distanceBetweenMarks = 40.0,
                font = Font.SERIF
            )
        )
        val cartesianSpace2 = CartesianSpaceDTO(
            id = UUID.randomUUID(),
            name = "Пространство2",
            functions = listOf(function2),
            xAxis = ConcatenationAxisDTO(
                id = UUID.randomUUID(),
                name = "Test2",
                backGroundColor = ColorUtils.getRandomColor(),
                delimeterColor = Color.BLACK,
                direction = ExistDirection(
                    Direction.BOTTOM,
                    null
                ),
                zeroPoint = SettingsProvider.getCanvasWidth() / 2,
                textSize = 10.0,
                distanceBetweenMarks = 40.0,
                font = Font.SERIF
            ),
            yAxis = ConcatenationAxisDTO(
                id = UUID.randomUUID(),
                name = "Test2",
                backGroundColor = ColorUtils.getRandomColor(),
                delimeterColor = Color.BLACK,
                direction = ExistDirection(
                    Direction.LEFT,
                    null
                ),
                zeroPoint = SettingsProvider.getCanvasHeight() / 2,
                textSize = 10.0,
                distanceBetweenMarks = 40.0,
                font = Font.SERIF
            )
        )

        return Pair(cartesianSpace, cartesianSpace2)
    }

}