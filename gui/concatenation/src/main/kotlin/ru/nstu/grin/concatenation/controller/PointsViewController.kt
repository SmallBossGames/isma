package ru.nstu.grin.concatenation.controller

import ru.nstu.grin.common.model.Point
import ru.nstu.grin.concatenation.events.FileCheckedEvent
import ru.nstu.grin.concatenation.model.FileReaderMode
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
            if (model.readerMode == FileReaderMode.SEQUENCE && coordinates.size % 2 != 0) {
                tornadofx.error("Неверное количество колонок")
                return@use emptyList()
            }
            result.add(coordinates)
        }
        result
    }

    fun sendFireCheckedEvent() {
        val points = when (model.readerMode) {
            FileReaderMode.ONE_TO_MANY -> {
                model.pointsList.map { list ->
                    list.zipWithNext { a, b ->
                        Point(
                            list[0].replace(",", ".").toDouble(),
                            b.replace(",", ".").toDouble()
                        )
                    }
                }
            }
            FileReaderMode.SEQUENCE -> {
                model.pointsList.map {
                    it.zipWithNext { a, b ->
                        Point(
                            a.replace(",", ".").toDouble(),
                            b.replace(",", ".").toDouble()
                        )
                    }
                }
            }
        }
        fire(
            FileCheckedEvent(
                points = points,
                addFunctionsMode = model.addFunctionsMode
            )
        )
    }
}