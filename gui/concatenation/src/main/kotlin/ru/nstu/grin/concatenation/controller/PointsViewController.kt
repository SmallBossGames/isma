package ru.nstu.grin.concatenation.controller

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
        val result = mutableListOf<List<String>>()
        for (line in lines) {
            val coordinates = line.split(model.delimiter)
            if (coordinates.size % 2 != 0) {
                tornadofx.error("Неверное количество колонок")
                return@use emptyList()
            }
            result.add(coordinates)
        }
        result
    }
}