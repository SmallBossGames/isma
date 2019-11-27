package ru.nstu.grin.settings

object SettingProvider {
    private const val CANVAS_WIDTH = 1200.0
    private const val CANVAS_HEIGHT = 800.0

    fun getCanvasWidth(): Double {
        return CANVAS_WIDTH
    }

    fun getCanvasHeight(): Double {
        return CANVAS_HEIGHT
    }
}