package ru.nstu.grin.concatenation.canvas.controller

import javafx.scene.Scene
import javafx.stage.Modality
import javafx.stage.Stage
import javafx.stage.Window
import org.koin.core.component.get
import org.koin.core.parameter.parametersOf
import ru.nstu.grin.common.converters.model.ArrowConverter
import ru.nstu.grin.common.dto.ArrowDTO
import ru.nstu.grin.common.model.Arrow
import ru.nstu.grin.concatenation.canvas.model.ConcatenationCanvasModel
import ru.nstu.grin.concatenation.canvas.model.ConcatenationCanvasViewModel
import ru.nstu.grin.concatenation.cartesian.model.CartesianSpace
import ru.nstu.grin.concatenation.description.model.DescriptionModalForCreate
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
) {

    fun replaceAll(
        cartesianSpaces: List<CartesianSpace>,
        arrows: List<Arrow>
    ){
        model.cartesianSpaces.setAll(cartesianSpaces)
        model.arrows.setAll(arrows)

        normalizeSpaces()
    }

    fun addArrow(arrow: ArrowDTO) {
        model.arrows.add(ArrowConverter.convert(arrow))
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

    fun normalizeSpaces(){
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

    fun openArrowModal(x: Double, y: Double, window: Window?) {
        // TODO: Disabled until migration to Koin
        /*find<ArrowModalView>(
            mapOf(
                ArrowModalView::type to ConcatenationType,
                ArrowModalView::x to x,
                ArrowModalView::y to y
            )
        ).openModal(stageStyle = StageStyle.UTILITY)*/
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

    fun unselectAll() {
        canvasViewModel.selectedFunctions.clear()
    }

    fun clearCanvas() {
        model.apply {
            cartesianSpaces.clear()
            arrows.clear()
        }
    }

    private companion object {
        const val DEFAULT_INDENT = 0.01
    }
}