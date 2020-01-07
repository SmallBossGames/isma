package ru.nstu.grin.controller

import ru.nstu.grin.model.DrawSize
import ru.nstu.grin.model.view.ManualEnterFunctionViewModel
import ru.nstu.grin.model.Point
import tornadofx.Controller

/**
 * @author Konstantin Volivach
 */
class ManualEnterFunctionController : Controller() {

    private val model: ManualEnterFunctionViewModel by inject()

    fun parseFunction(): List<Point> {
        val split = model.xPoints.split(" ")

        val result = mutableListOf<Point>()
        for (item in split) {
            val numbers = item.split(",")
            val point = Point(numbers[0].toDouble(), numbers[1].toDouble())
            result.add(point)
        }
        return result
    }

    fun addFunction(function: List<Point>) {
        TODO("think about common controller or OOP method")
    }

    private fun calculateDeltas(drawSize: DrawSize): Double {
        return drawSize.maxX - drawSize.minX
    }
}