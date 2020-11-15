package ru.nstu.grin.simple.controller

import ru.nstu.grin.simple.model.FileOptionsModel
import ru.nstu.grin.simple.model.PointsViewModel
import ru.nstu.grin.simple.view.modal.PointsView
import tornadofx.Controller

class FileOptionsController : Controller() {
    private val model: FileOptionsModel by inject(params = params)

    fun openFile() {
        find<PointsView>(
            mapOf(
                PointsViewModel::file to model.file,
                PointsViewModel::delimiter to model.delimiter
            )
        ).openModal()
    }
}