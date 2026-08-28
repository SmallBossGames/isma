package ru.nstu.grin.concatenation.file.options.controller

import org.koin.core.scope.Scope
import ru.nstu.grin.concatenation.points.view.PointsView

class FileOptionsController(
    private val koinScope: Scope,
) {
    fun openPointsWindow() {
        PointsView.openModal(koinScope)
    }
}