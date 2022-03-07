package ru.nstu.grin.concatenation.canvas.view

import javafx.scene.control.Tab
import javafx.scene.control.TabPane

class CanvasToolBar(
    chartToolBar: ChartToolBar,
    modesToolBar: ModesToolBar,
    mathToolBar: MathToolBar,
    transformToolBar: TransformToolBar,
): TabPane(
    Tab("Chart", chartToolBar),
    Tab("Modes", modesToolBar),
    Tab("Math", mathToolBar),
    Tab("Transform", transformToolBar)
) {
    init {
        tabClosingPolicy = TabClosingPolicy.UNAVAILABLE
    }
}