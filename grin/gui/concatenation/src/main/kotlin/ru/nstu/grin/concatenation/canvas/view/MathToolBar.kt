package ru.nstu.grin.concatenation.canvas.view

import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.ToolBar
import javafx.scene.control.Tooltip
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.stage.Modality
import javafx.stage.Stage
import javafx.stage.Window
import org.koin.core.component.KoinScopeComponent
import org.koin.core.component.get
import org.koin.core.parameter.parametersOf
import ru.nstu.grin.concatenation.canvas.model.ConcatenationCanvasViewModel
import ru.nstu.grin.concatenation.function.controller.DerivativeFunctionController
import ru.nstu.grin.concatenation.function.view.FunctionIntegrationView
import ru.nstu.grin.concatenation.function.view.FunctionWaveletView
import ru.nstu.grin.concatenation.function.view.IntersectionFunctionView
import ru.nstu.grin.concatenation.koin.FunctionIntegrationModalScope
import ru.nstu.grin.concatenation.koin.FunctionWaveletModalScope
import ru.nstu.grin.concatenation.koin.MainGrinScope
import ru.nstu.grin.concatenation.koin.SearchIntersectionsModalScope

class MathToolBar(
    canvasViewModel: ConcatenationCanvasViewModel,
    derivativeFunctionController: DerivativeFunctionController,
    mainGrinScope: MainGrinScope,
): ToolBar(
    Button(null, ImageView(Image("intersection.png")).apply {
        fitWidth = 20.0
        fitHeight = 20.0
    }).apply {
        tooltip = Tooltip("Find intersections")

        setOnAction {
            val searchIntersectionsScope = mainGrinScope.get<SearchIntersectionsModalScope>()
            val view = searchIntersectionsScope.get<IntersectionFunctionView>()

            val window = scene.window

            openModal(view, window, searchIntersectionsScope, view.title)
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

        setOnAction {
            val selectedFunction = canvasViewModel.selectedFunctions.firstOrNull() ?: return@setOnAction

            val functionIntegrationModalScope = mainGrinScope.get<FunctionWaveletModalScope>()
            val view = functionIntegrationModalScope.get<FunctionWaveletView>{ parametersOf(selectedFunction) }

            val window = scene.window

            openModal(view, window, functionIntegrationModalScope, view.title)
        }

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
            val selectedFunction = canvasViewModel.selectedFunctions.firstOrNull() ?: return@setOnAction

            val functionIntegrationModalScope = mainGrinScope.get<FunctionIntegrationModalScope>()
            val view = functionIntegrationModalScope.get<FunctionIntegrationView>{ parametersOf(selectedFunction) }

            val window = scene.window

            openModal(view, window, functionIntegrationModalScope, view.title)
        }
    },
)

private fun openModal(
    view: Parent,
    window: Window?,
    searchIntersectionsScope: KoinScopeComponent,
    windowTitle: String
) {
    Stage().apply {
        scene = Scene(view)
        title = windowTitle

        initModality(Modality.WINDOW_MODAL)

        if (window != null) {
            initOwner(window)
        }

        setOnCloseRequest {
            searchIntersectionsScope.scope.close()
        }

        show()
    }
}