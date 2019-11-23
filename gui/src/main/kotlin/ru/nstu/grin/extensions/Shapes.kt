package ru.nstu.grin.extensions

import javafx.scene.Node
import javafx.scene.Parent
import javafx.scene.paint.Color
import ru.nstu.grin.model.Arrow
import tornadofx.attachTo

fun Parent.arrow(color: Color, x: Double, y: Double, op: Node.() -> Unit = {}) = Arrow(
    color = color,
    x = x,
    y = y
).getShape().attachTo(this, op)