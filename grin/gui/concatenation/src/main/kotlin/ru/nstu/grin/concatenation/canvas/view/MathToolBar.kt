package ru.nstu.grin.concatenation.canvas.view

import javafx.scene.control.Button
import javafx.scene.control.ToolBar
import javafx.scene.control.Tooltip
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import ru.nstu.grin.concatenation.canvas.model.ConcatenationCanvasModel
import ru.nstu.grin.concatenation.function.view.DerivativeFunctionFragment
import ru.nstu.grin.concatenation.function.view.FunctionIntegrationFragment
import ru.nstu.grin.concatenation.function.view.IntersectionFunctionFragment
import ru.nstu.grin.concatenation.function.view.WaveletFunctionFragment
import tornadofx.Scope
import tornadofx.find

class MathToolBar(
    scope: Scope,
    model: ConcatenationCanvasModel,
    drawer: ConcatenationChainDrawer,
): ToolBar(
    Button(null, ImageView(Image("intersection.png")).apply {
        fitWidth = 20.0
        fitHeight = 20.0
    }).apply {
        tooltip = Tooltip("Find intersections")

        setOnAction {
            find<IntersectionFunctionFragment>(scope).openModal()
        }
    },
    Button(null, ImageView(Image("derivative.png")).apply {
        fitWidth = 20.0
        fitHeight = 20.0
    }).apply {
        tooltip = Tooltip("Apply derivative")

        setOnAction {
            val function = model.selectedFunction
            if (function != null) {
                val derivativeDetails = function.derivativeDetails
                if (derivativeDetails != null) {
                    function.derivativeDetails = null
                    drawer.draw()
                    return@setOnAction
                }
                find<DerivativeFunctionFragment>(
                    scope,
                    mapOf(
                        "function" to function
                    )
                ).openModal()
            }
        }
    },
    Button(null, ImageView(Image("wavelet.png")).apply {
        fitWidth = 20.0
        fitHeight = 20.0
    }).apply {
        tooltip = Tooltip("Apply wavelet")

        setOnAction {
            val function = model.selectedFunction
            if (function != null) {
                val waveletDetails = function.waveletDetails
                if (waveletDetails != null) {
                    function.waveletDetails = null
                    drawer.draw()
                    return@setOnAction
                }
                find<WaveletFunctionFragment>(
                    scope,
                    mapOf(
                        "function" to function
                    )
                ).openModal()
            }
        }
    },
    Button(null, ImageView(Image("integral.png")).apply {
        fitWidth = 20.0
        fitHeight = 20.0
    }).apply {
        tooltip = Tooltip("Find integral")

        setOnAction {
            val function = model.selectedFunction
            if (function != null) {
                find<FunctionIntegrationFragment>(
                    scope,
                    mapOf(
                        "function" to function.id
                    )
                ).openModal()
            }
        }
    },
)