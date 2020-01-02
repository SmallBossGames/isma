package ru.nstu.grin.generators

import javafx.scene.paint.Color
import ru.kontur.kinfra.kfixture.api.ValidationConstructor

class ColorConstructor : ValidationConstructor<Color> {
    override fun call(): Color {
        return Color.ALICEBLUE
    }
}