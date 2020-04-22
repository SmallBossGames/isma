package ru.nstu.grin.concatenation.controller

import ru.nstu.grin.concatenation.model.FileOptionsModel
import ru.nstu.grin.concatenation.model.PointsViewModel
import ru.nstu.grin.concatenation.view.modal.PointsView
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