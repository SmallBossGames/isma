package ru.nstu.grin.concatenation.file.options.controller

import ru.nstu.grin.concatenation.function.model.FileModel
import ru.nstu.grin.concatenation.function.model.FileType
import ru.nstu.grin.concatenation.points.view.PointsView
import tornadofx.Controller

class FileOptionsController : Controller() {
    private val fileModel: FileModel by inject()

    fun openPointsWindow() {
        find<PointsView>().openModal()
    }

    fun getType(): FileType? {
        val fileName = fileModel.file.name.substringAfterLast(".")
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