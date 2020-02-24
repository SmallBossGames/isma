package ru.nstu.grin.extensions

import javafx.scene.Parent
import ru.nstu.grin.model.drawable.ConcatenationFunction
import tornadofx.attachTo

fun Parent.function(function: ConcatenationFunction) = function.getShape().attachTo(this)