package ru.nstu.grin.concatenation.controller

import ru.nstu.grin.common.model.Point
import ru.nstu.grin.concatenation.model.PointsViewModel
import tornadofx.Controller
import java.io.File
import java.io.FileInputStream

class PointsViewController : Controller() {
    private val model: PointsViewModel by inject(params = params)

    fun readPoints() {
        model.pointsListProperty.clear()
        model.pointsListProperty.addAll(readPoints(model.file))
    }

    private fun readPoints(file: File): List<List<String>> = FileInputStream(file).use {
        val lines = String(it.readBytes()).split("\n")
        lines.mapNotNull { line ->
            when {
                line.contains(",") -> {
                    extractPoints(line, ",")
                }
                line.contains(";") -> {
                    extractPoints(line, ";")
                }
                else -> null
            }
        }
    }

    private fun extractPoints(line: String, delimiter: String): List<String> {
        val coordinates = line.split(delimiter)
        if (coordinates.size % 2 != 0) {
            tornadofx.error("Неверное колличество колонок")
            return emptyList()
        }
        return coordinates
    }
}