package ru.nstu.grin.simple.controller

import ru.nstu.grin.common.model.Point
import ru.nstu.grin.simple.model.PointsViewModel
import tornadofx.Controller
import java.io.File
import java.io.FileInputStream

class PointsViewController : Controller() {
    private val model: PointsViewModel by inject(params = params)

    fun readPoints() {
        model.pointsProperty.clear()
        model.pointsProperty.addAll(readPoints(model.file))
    }

    private fun readPoints(file: File): List<Point> = FileInputStream(file).use {
        val lines = String(it.readBytes()).split("\n")
        lines.mapNotNull { line ->
            when {
                line.contains(",") -> {
                    val coordinates = line.split(",")
                    Point(coordinates[0].toDouble(), coordinates[1].toDouble())
                }
                line.contains(";") -> {
                    val coordinates = line.split(";")
                    Point(coordinates[0].toDouble(), coordinates[1].toDouble())
                }
                else -> null
            }
        }
    }
}