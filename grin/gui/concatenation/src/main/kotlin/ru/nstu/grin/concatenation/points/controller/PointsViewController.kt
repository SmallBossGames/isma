package ru.nstu.grin.concatenation.points.controller

import ru.nstu.grin.common.model.Point
import ru.nstu.grin.concatenation.points.events.FileCheckedEvent
import ru.nstu.grin.concatenation.file.options.model.FileReaderMode
import ru.nstu.grin.concatenation.points.model.PointsViewModel
import ru.nstu.grin.concatenation.file.options.model.CsvDetails
import ru.nstu.grin.concatenation.file.options.model.ExcelDetails
import ru.nstu.grin.concatenation.file.readers.*
import ru.nstu.grin.concatenation.function.model.FileModel
import ru.nstu.grin.concatenation.function.model.FileType
import tornadofx.Controller
import java.io.File

class PointsViewController : Controller() {
    private val model: PointsViewModel by inject()
    private val fileModel: FileModel by inject()

    fun readPoints() {
        model.pointsListProperty.setAll(readPoints(fileModel.file))
    }

    private fun readPoints(file: File): List<List<String>> {
        val fileType = FileRecognizer.recognize(file)
        return when (val details = fileModel.details) {
            is ExcelDetails -> {
                if (fileType == FileType.XLSX) {
                    return XLSXReader().read(file, details.sheetName, ExcelRange(details.range))
                }
                if (fileType == FileType.XLS) {
                    return XLSReader().read(file, details.sheetName, ExcelRange(details.range))
                }
                throw IllegalArgumentException("Something went wrong can't match details and fileType")
            }
            is CsvDetails -> {
                CsvReader().read(file, details.delimiter, fileModel.readerMode)
            }
        }
    }

    private fun <T> List<List<T>>.transpose(): List<List<T>> {
        if (this.isEmpty()) return this

        val result = mutableListOf<List<T>>()
        for (i in this[0].indices) {
            val column = mutableListOf<T>()
            for (row in this) {
                if (row.size <= i) continue
                column.add(row[i])
            }
            result.add(column)
        }
        return result
    }

    fun sendFireCheckedEvent() {
        val points = when (fileModel.readerMode) {
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
        }.transpose()

        fire(
            FileCheckedEvent(
                points = points,
                addFunctionsMode = model.addFunctionsMode
            )
        )
    }
}