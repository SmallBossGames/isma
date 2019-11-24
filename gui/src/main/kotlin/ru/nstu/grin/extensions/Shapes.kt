package ru.nstu.grin.extensions

import javafx.scene.Node
import javafx.scene.Parent
import javafx.scene.paint.Color
import ru.nstu.grin.Direction
import ru.nstu.grin.model.Arrow
import ru.nstu.grin.model.Function
import ru.nstu.grin.model.Point
import tornadofx.attachTo

fun Parent.arrow(color: Color, x: Double, y: Double) = Arrow(
    color = color,
    x = x,
    y = y
).getShape().attachTo(this)

fun Parent.arrow(arrow: Arrow) = arrow.getShape().attachTo(this)

fun Parent.function(
    pointArray: List<Point>,
    xDirection: Direction,
    yDirection: Direction,
    minDelta: Double,
    delta: Double,
    functionColor: Color,
    xAxisColor: Color,
    yAxisColor: Color,
    op: Node.() -> Unit = {}
) = Function(
    pointArray = pointArray,
    xDirection = xDirection,
    yDirection = yDirection,
    minDelta = minDelta,
    delta = delta,
    functionColor = functionColor,
    xAxisColor = xAxisColor,
    yAxisColor = yAxisColor
).getShape().attachTo(this, op)

fun Parent.function(function: Function) = function.getShape().attachTo(this)