package ru.nstu.grin.integration

import javafx.scene.Scene
import javafx.scene.image.Image
import javafx.scene.paint.Color
import javafx.stage.Modality
import javafx.stage.Stage
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
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


class GrinIntegrationFacade: KoinComponent {
    fun open(spaces: List<CartesianSpace>) {
        val initData = InitCanvasData(
            cartesianSpaces = spaces,
        )

        Stage().apply {
            val scope = MainGrinScope(this)
            val view = scope.get<ConcatenationView>{ parametersOf(initData) }

            scene = Scene(view)
            title = "GrIn"

            icons.add(Image("/ru/nstu/grin/integration/isma-2016-title.png"))

            initModality(
                Modality.WINDOW_MODAL
            )
            setOnCloseRequest {
                scope.scope.close()
            }
            show()
        }
    }

    fun openSimpleChart(
        functions: Collection<FunctionModel>,
        xAxisName: String = "X axis",
        yAxisName: String = "Y axis",
    ) {
        val xAxis = ConcatenationAxis(
            name = xAxisName,
            direction = Direction.BOTTOM,
        )

        val yAxis = ConcatenationAxis(
            name = yAxisName,
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
            mutableListOf(),
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
            name = name,
            xPoints = points.map { it.x }.toDoubleArray(),
            yPoints = points.map { it.y }.toDoubleArray(),
            isHide = false,
            functionColor = color,
            lineSize = 2.0,
            lineType = LineType.POLYNOM
        )
    }
}