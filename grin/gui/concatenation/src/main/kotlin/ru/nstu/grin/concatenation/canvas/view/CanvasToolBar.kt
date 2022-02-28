package ru.nstu.grin.concatenation.canvas.view

import javafx.scene.control.Tab
import javafx.scene.control.TabPane

class CanvasToolBar(
    chartToolBar: ChartToolBar,
    modesPanel: ModesPanel,
    mathPanel: MathPanel,
    transformPanel: TransformPanel,
): TabPane(
    Tab("Chart", chartToolBar),
    Tab("Modes", modesPanel.root),
    Tab("Math", mathPanel.root),
    Tab("Transform", transformPanel.root)
)