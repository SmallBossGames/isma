package ru.nstu.grin.integration

import javafx.scene.Scene
import javafx.scene.paint.Color
import javafx.stage.Modality
import javafx.stage.Stage
import org.koin.core.component.KoinComponent
import org.koin.core.parameter.parametersOf
import ru.nstu.grin.common.model.Point
import ru.nstu.grin.concatenation.axis.model.ConcatenationAxis
import ru.nstu.grin.concatenation.axis.model.Direction
import ru.nstu.grin.concatenation.canvas.model.InitCanvasData
import ru.nstu.grin.concatenation.canvas.view.ConcatenationView
import ru.nstu.grin.concatenation.cartesian.model.CartesianSpace
import ru.nstu.grin.concatenation.function.model.ConcatenationFunction
import ru.nstu.grin.concatenation.function.model.LineType
import ru.nstu.grin.concatenation.koin.MainGrinScope
import java.util.*


class GrinIntegrationFacade: KoinComponent {
    fun open(spaces: List<CartesianSpace>) {
        val initData = InitCanvasData(
            cartesianSpaces = spaces,
            arrows = listOf(),
            descriptions = listOf()
        )

        Stage().apply {
            val scopeInstance = MainGrinScope(this)
            val scope = scopeInstance.scope
            val view = scope.get<ConcatenationView>{ parametersOf(initData) }

            scene = Scene(view)
            title = "GRIN"
            initModality(
                Modality.WINDOW_MODAL
            )
            setOnCloseRequest {
                scopeInstance.closeScope()
            }
            show()
        }
    }

    fun openSimpleChart(functions: Collection<FunctionModel>) {
        val xAxis = ConcatenationAxis(
            name = "X axis",
            direction = Direction.BOTTOM,
        )

        val yAxis = ConcatenationAxis(
            name = "Y axis",
            direction = Direction.LEFT,
        )

        val step = 360.0 / functions.size

        val mappedFunctions = functions.mapIndexed { i, function ->
            createSimpleConcatenationFunction(
                function.name,
                function.points.map { Point(it.x, it.y) },
                Color.hsb(step*i,1.0, 0.8)
            )
        }.toMutableList()

        val cartesianSpace = CartesianSpace(
            "CartesianSpace",
            mappedFunctions,
            xAxis,
            yAxis
        )

        open(listOf(cartesianSpace))
    }

    private fun createSimpleConcatenationFunction(
        name: String,
        points: List<Point>,
        color: Color
    ) : ConcatenationFunction {
        return ConcatenationFunction(
            id = UUID.randomUUID(),
            name = name,
            points = points,
            isHide = false,
            isSelected = false,
            functionColor = color,
            lineSize = 2.0,
            lineType = LineType.POLYNOM
        )
    }
}