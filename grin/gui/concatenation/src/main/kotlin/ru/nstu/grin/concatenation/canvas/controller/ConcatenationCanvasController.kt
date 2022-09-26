package ru.nstu.grin.concatenation.canvas.controller

import javafx.scene.Scene
import javafx.scene.paint.Color
import javafx.stage.Modality
import javafx.stage.Stage
import javafx.stage.Window
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import org.koin.core.component.get
import org.koin.core.parameter.parametersOf
import ru.nstu.grin.concatenation.canvas.model.ConcatenationCanvasModel
import ru.nstu.grin.concatenation.canvas.model.ConcatenationCanvasViewModel
import ru.nstu.grin.concatenation.cartesian.model.CartesianSpace
import ru.nstu.grin.concatenation.description.model.DescriptionDto
import ru.nstu.grin.concatenation.description.model.DescriptionModalForCreate
import ru.nstu.grin.concatenation.description.service.DescriptionCanvasService
import ru.nstu.grin.concatenation.description.view.ChangeDescriptionView
import ru.nstu.grin.concatenation.function.view.AddFunctionModalView
import ru.nstu.grin.concatenation.koin.AddFunctionModalScope
import ru.nstu.grin.concatenation.koin.DescriptionChangeModalScope
import ru.nstu.grin.concatenation.koin.MainGrinScope
import kotlin.math.max
import kotlin.math.min

class ConcatenationCanvasController(
    private val model: ConcatenationCanvasModel,
    private val canvasViewModel: ConcatenationCanvasViewModel,
    private val mainGrinScope: MainGrinScope,
    private val descriptionCanvasService: DescriptionCanvasService,
) {
    private val coroutineScope = CoroutineScope(Dispatchers.Default)

    fun replaceAll(
        cartesianSpaces: List<CartesianSpace>,
        normalizeSpaces: Boolean = false
    ){
        model.cartesianSpaces.clear()
        model.cartesianSpaces.addAll(cartesianSpaces)

        coroutineScope.launch {
            if(normalizeSpaces) normalizeSpaces()

            model.reportAxesListUpdate()
            model.reportCartesianSpacesListUpdate()
            model.reportDescriptionsListUpdate()
            model.reportFunctionsListUpdate()
        }
    }

    fun openFunctionModal(window: Window?) {
        val modalScope = mainGrinScope.get<AddFunctionModalScope>()
        val view = modalScope.get<AddFunctionModalView>()

        Stage().apply {
            scene = Scene(view)
            title = "Add Function"
            isIconified = false

            initModality(Modality.WINDOW_MODAL)

            if(window != null){
                initOwner(window)
            }

            setOnCloseRequest {
                modalScope.closeScope()
            }

            show()
        }
    }

    suspend fun normalizeSpaces() = coroutineScope {
        model.cartesianSpaces.forEach { space ->
            var minX = Double.POSITIVE_INFINITY
            var maxX = Double.NEGATIVE_INFINITY

            var minY = Double.POSITIVE_INFINITY
            var maxY = Double.NEGATIVE_INFINITY

            space.functions.forEach{ function ->
                val (xPoints, yPoints) = function.transformedPoints

                for (x in xPoints) {
                    minX = min(x, minX)
                    maxX = max(x, maxX)
                }

                for (y in yPoints) {
                    minY = min(y, minY)
                    maxY = max(y, maxY)
                }
            }

            val indentX = (maxX - minX) * DEFAULT_INDENT
            val indentY = (maxY - minY) * DEFAULT_INDENT

            space.xAxis.scaleProperties = space.xAxis.scaleProperties.copy(
                minValue = minX - indentX,
                maxValue = maxX + indentX,
            )

            space.yAxis.scaleProperties = space.yAxis.scaleProperties.copy(
                minValue = minY - indentY,
                maxValue = maxY + indentY,
            )
        }
    }

    fun openDescriptionModal(space: CartesianSpace, x: Double, y: Double, window: Window? = null) {
        val descriptionChangeModalScope = mainGrinScope.get<DescriptionChangeModalScope>()

        val initData = DescriptionModalForCreate(space, x, y)
        val view = descriptionChangeModalScope.get<ChangeDescriptionView> { parametersOf(initData) }

        Stage().apply {
            scene = Scene(view)
            title = "Add Description"
            initModality(Modality.WINDOW_MODAL)

            if(window != null){
                initOwner(window)
            }

            setOnCloseRequest {
                descriptionChangeModalScope.closeScope()
            }

            show()
        }
    }

    fun addPointDescription(space: CartesianSpace, pointX: Double, pointY: Double){
        val description = DescriptionDto(
            space = space,
            text = "Point (${String.format("%.3f", pointX)}; ${String.format("%.3f", pointY)})",
            textOffsetX = 30.0,
            textOffsetY = 30.0,
            color = Color.BLACK,
            font = "Arial",
            textSize = 12.0,
            pointerX = pointX,
            pointerY = pointY
        )

        descriptionCanvasService.add(description)
    }

    fun unselectAll() {
        canvasViewModel.selectedFunctions.clear()
    }

    fun clearCanvas() {
        model.apply {
            cartesianSpaces.clear()
        }
    }

    private companion object {
        const val DEFAULT_INDENT = 0.01
    }
}