package ru.nstu.grin.concatenation.canvas.model

import javafx.geometry.Insets
import javafx.scene.canvas.GraphicsContext
import ru.nstu.grin.concatenation.description.model.Description
import ru.nstu.grin.concatenation.function.model.ConcatenationFunction
import ru.nstu.grin.concatenation.points.model.PointToolTipsSettings

class ConcatenationCanvasViewModel {
    lateinit var functionsLayerContext: GraphicsContext
    lateinit var uiLayerContext: GraphicsContext
    var canvasWidth = 0.0
    var canvasHeight = 0.0
    var functionsArea = Insets.EMPTY!!

    val selectedFunctions = HashSet<ConcatenationFunction>()
    val selectedDescriptions = HashSet<Description>()
    val pointToolTipSettings = PointToolTipsSettings(false, mutableSetOf())
}