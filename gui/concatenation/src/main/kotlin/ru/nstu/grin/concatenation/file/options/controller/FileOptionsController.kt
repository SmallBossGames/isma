package ru.nstu.grin.concatenation.file.options.controller

import ru.nstu.grin.concatenation.file.options.model.FileOptionsModel
import ru.nstu.grin.concatenation.points.model.PointsViewModel
import ru.nstu.grin.concatenation.points.view.PointsView
import tornadofx.Controller

class FileOptionsController : Controller() {
    private val model: FileOptionsModel by inject(params = params)

    fun openPointsWindow() {
        find<PointsView>(
            mapOf(
                PointsViewModel::file to model.file,
                PointsViewModel::delimiter to model.delimiter,
                PointsViewModel::readerMode to model.readerMode
            )
        ).openModal()
    }
}