package ru.nstu.grin.utils

import javafx.scene.paint.Color
import kotlin.random.Random

object ColorUtils {
    private const val DEFAULT_OPACITY = 1.0
    private const val MAX_RGB = 256

    fun getRandomColor(): Color {
        return Color.rgb(Random.nextInt(MAX_RGB), Random.nextInt(MAX_RGB), Random.nextInt(MAX_RGB), DEFAULT_OPACITY)
    }
}