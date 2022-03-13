package ru.nstu.grin.concatenation.file.options.controller

import ru.nstu.grin.concatenation.points.view.PointsView
import tornadofx.Controller

class FileOptionsController : Controller() {
    fun openPointsWindow() {
        find<PointsView>().openModal()
    }
}