package ru.nstu.grin.extensions

import javafx.scene.Node
import javafx.scene.Parent
import javafx.scene.paint.Color
import ru.nstu.grin.model.drawable.Arrow
import ru.nstu.grin.model.drawable.Axis
import ru.nstu.grin.model.drawable.Function
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
    xAxis: Axis,
    yAxis: Axis,
    functionColor: Color,
    op: Node.() -> Unit = {}
) = Function(
    pointArray = pointArray,
    xAxis = xAxis,
    yAxis = yAxis,
    functionColor = functionColor
).getShape().attachTo(this, op)

fun Parent.function(function: Function) = function.getShape().attachTo(this)