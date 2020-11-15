package ru.nstu.grin

import tornadofx.App
import tornadofx.UIComponent
import tornadofx.launch
import tornadofx.reloadStylesheetsOnFocus
import kotlin.reflect.KClass

class StateChartApp : App() {
    override val primaryView: KClass<out UIComponent> = TODO()

    init {
        reloadStylesheetsOnFocus()
    }
}

fun main(args: Array<String>) {
    launch<StateChartApp>(args = args)
}