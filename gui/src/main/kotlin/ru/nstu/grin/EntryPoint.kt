package ru.nstu.grin

import tornadofx.App
import tornadofx.UIComponent
import tornadofx.launch
import tornadofx.reloadStylesheetsOnFocus
import kotlin.reflect.KClass

class MyApp : App() {
    override val primaryView: KClass<out UIComponent> = MainView::class



    init {
//        parameters.named[0]

        reloadStylesheetsOnFocus()
    }
}

fun main(args: Array<String>) {
    launch<MyApp>(args = args)
}