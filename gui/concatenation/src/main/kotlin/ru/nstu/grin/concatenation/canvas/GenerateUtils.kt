package ru.nstu.grin.concatenation.canvas

import javafx.scene.paint.Color
import ru.nstu.grin.common.common.SettingsProvider
import ru.nstu.grin.common.model.Point
import ru.nstu.grin.common.utils.ColorUtils
import ru.nstu.grin.concatenation.axis.dto.ConcatenationAxisDTO
import ru.nstu.grin.concatenation.axis.model.Direction
import ru.nstu.grin.concatenation.canvas.dto.CartesianSpaceDTO
import ru.nstu.grin.concatenation.canvas.model.CartesianSpace
import ru.nstu.grin.concatenation.canvas.model.ExistDirection
import ru.nstu.grin.concatenation.function.controller.DeltaMarksGenerator
import ru.nstu.grin.concatenation.function.controller.DeltaSizeCalculator
import ru.nstu.grin.concatenation.function.dto.ConcatenationFunctionDTO
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

    fun generateTwoCartesianSpaces(): Pair<CartesianSpaceDTO, CartesianSpaceDTO> {
        val function = ConcatenationFunctionDTO(
            id = UUID.randomUUID(),
            name = "Test",
            points = generateCircle(radius = 2.0),
            functionColor = Color.BLACK
        )

        val function2 = ConcatenationFunctionDTO(
            id = UUID.randomUUID(),
            name = "Test",
            points = generateCircle(radius = 2.0),
            functionColor = Color.BLACK
        )

        val cartesianSpace = CartesianSpaceDTO(
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
                zeroPoint = SettingsProvider.getCanvasWidth() / 2
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
                zeroPoint = SettingsProvider.getCanvasHeight() / 2
            )
        )
        val cartesianSpace2 = CartesianSpaceDTO(
            functions = listOf(function),
            xAxis = ConcatenationAxisDTO(
                id = UUID.randomUUID(),
                name = "Test2",
                backGroundColor = ColorUtils.getRandomColor(),
                delimeterColor = Color.BLACK,
                direction = ExistDirection(
                    Direction.BOTTOM,
                    null
                ),
                zeroPoint = SettingsProvider.getCanvasWidth() / 2
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
                zeroPoint = SettingsProvider.getCanvasHeight() / 2
            )
        )

        return Pair(cartesianSpace, cartesianSpace2)
    }

}