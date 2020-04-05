package ru.nstu.grin.main

import javafx.stage.Stage
import ru.nstu.grin.concatenation.view.ConcatenationView
import ru.nstu.grin.main.view.ConstuctorView
import tornadofx.App
import tornadofx.UIComponent
import tornadofx.launch
import tornadofx.reloadStylesheetsOnFocus
import kotlin.reflect.KClass

class MyApp : App() {
    override val primaryView: KClass<out UIComponent> = ConcatenationView::class

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