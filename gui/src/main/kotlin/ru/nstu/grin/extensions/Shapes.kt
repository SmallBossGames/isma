package ru.nstu.grin.extensions

import javafx.scene.Parent
import ru.nstu.grin.model.drawable.Function
import tornadofx.attachTo

fun Parent.function(function: Function) = function.getShape().attachTo(this)