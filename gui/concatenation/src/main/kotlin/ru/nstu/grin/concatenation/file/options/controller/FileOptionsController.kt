package ru.nstu.grin.concatenation.file.options.controller

import ru.nstu.grin.concatenation.file.options.model.FileOptionsModel
import ru.nstu.grin.concatenation.function.model.FileType
import ru.nstu.grin.concatenation.points.model.PointsViewModel
import ru.nstu.grin.concatenation.points.view.PointsView
import tornadofx.Controller

class FileOptionsController : Controller() {
    private val model: FileOptionsModel by inject(params = params)

    fun openPointsWindow() {
        find<PointsView>(
            mapOf(
                PointsViewModel::file to model.file,
                PointsViewModel::details to model.details,
                PointsViewModel::readerMode to model.readerMode
            )
        ).openModal()
    }

    fun getType(): FileType? {
        val fileName = model.file.name.substringAfterLast(".")
        return when (fileName) {
            "xls" -> {
                FileType.XLS
            }
            "xlsx" -> {
                FileType.XLSX
            }
            "csv" -> {
                FileType.CSV
            }
            else -> {
                tornadofx.error("Неправильный формат файла")
                return null
            }
        }
    }
}