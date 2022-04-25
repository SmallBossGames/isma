package ru.nstu.grin.concatenation.canvas.view

import javafx.scene.control.Button
import javafx.scene.control.ToolBar
import javafx.scene.control.Tooltip
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import ru.nstu.grin.concatenation.canvas.model.ConcatenationCanvasViewModel
import ru.nstu.grin.concatenation.function.controller.DerivativeFunctionController
import ru.nstu.grin.concatenation.function.view.FunctionIntegrationFragment
import ru.nstu.grin.concatenation.function.view.IntersectionFunctionFragment
import tornadofx.Scope
import tornadofx.find

class MathToolBar(
    scope: Scope,
    canvasViewModel: ConcatenationCanvasViewModel,
    derivativeFunctionController: DerivativeFunctionController,
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
            canvasViewModel.selectedFunctions.forEach { function ->
                derivativeFunctionController.applyDerivative(function)
            }
        }
    },
    Button(null, ImageView(Image("wavelet.png")).apply {
        fitWidth = 20.0
        fitHeight = 20.0
    }).apply {
        tooltip = Tooltip("Apply wavelet")

        //TODO: disabled until migration to Async Transformers
        /*setOnAction {
            val function = canvasViewModel.selectedFunctions.firstOrNull()
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
        }*/
    },
    Button(null, ImageView(Image("integral.png")).apply {
        fitWidth = 20.0
        fitHeight = 20.0
    }).apply {
        tooltip = Tooltip("Find integral")

        setOnAction {
            val function = canvasViewModel.selectedFunctions.firstOrNull()
            if (function != null) {
                find<FunctionIntegrationFragment>(
                    scope
                ).openModal()
            }
        }
    },
)