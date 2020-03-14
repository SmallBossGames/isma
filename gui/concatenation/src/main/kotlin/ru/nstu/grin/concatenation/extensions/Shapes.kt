package ru.nstu.grin.concatenation.extensions

import javafx.scene.Parent
import ru.nstu.grin.concatenation.model.ConcatenationFunction
import tornadofx.attachTo

fun Parent.function(function: ConcatenationFunction) = function.getShape().attachTo(this)