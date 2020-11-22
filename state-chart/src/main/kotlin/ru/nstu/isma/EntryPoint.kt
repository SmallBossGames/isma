package ru.nstu.isma

import ru.nstu.isma.view.StateChartView
import tornadofx.App
import tornadofx.UIComponent
import tornadofx.launch
import tornadofx.reloadStylesheetsOnFocus
import kotlin.reflect.KClass

class StateChartApp : App() {
    override val primaryView: KClass<out UIComponent> = StateChartView::class

    init {
        reloadStylesheetsOnFocus()
    }
}

fun main(args: Array<String>) {
    launch<StateChartApp>(args = args)
}