package ru.nstu.grin.main

import javafx.stage.Stage
import jwave.Transform
import jwave.tools.MathToolKit
import jwave.transforms.AncientEgyptianDecomposition
import jwave.transforms.FastWaveletTransform
import jwave.transforms.wavelets.haar.Haar1
import ru.nstu.grin.concatenation.view.ConcatenationCanvas
import ru.nstu.grin.concatenation.view.ConcatenationView
import ru.nstu.grin.simple.view.SimpleCanvasView
import tornadofx.App
import tornadofx.UIComponent
import tornadofx.launch
import tornadofx.reloadStylesheetsOnFocus
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat
import kotlin.math.pow
import kotlin.reflect.KClass

class MyApp : App() {
    override val primaryView: KClass<out UIComponent> = SimpleCanvasView::class

    init {
        reloadStylesheetsOnFocus()
    }

    override fun start(stage: Stage) {
        stage.isResizable = false
        super.start(stage)
    }
}

fun main(args: Array<String>) {
    launch<MyApp>(args = args)
}

