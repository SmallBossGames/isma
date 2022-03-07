package ru.nstu.grin.concatenation.canvas.controller

import javafx.scene.Scene
import javafx.stage.Modality
import javafx.stage.Stage
import javafx.stage.StageStyle
import javafx.stage.Window
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.component.get
import org.koin.core.parameter.parametersOf
import ru.nstu.grin.common.converters.model.ArrowConverter
import ru.nstu.grin.common.dto.ArrowDTO
import ru.nstu.grin.common.model.Arrow
import ru.nstu.grin.common.model.ConcatenationType
import ru.nstu.grin.common.model.Description
import ru.nstu.grin.concatenation.arrow.view.ArrowModalView
import ru.nstu.grin.concatenation.canvas.model.ConcatenationCanvasModel
import ru.nstu.grin.concatenation.canvas.model.ExistDirection
import ru.nstu.grin.concatenation.cartesian.model.CartesianSpace
import ru.nstu.grin.concatenation.description.model.DescriptionModalInitData
import ru.nstu.grin.concatenation.description.view.ChangeDescriptionView
import ru.nstu.grin.concatenation.function.view.AddFunctionModalView
import ru.nstu.grin.concatenation.koin.DescriptionChangeModalScope
import ru.nstu.grin.concatenation.koin.MainGrinScopeWrapper
import tornadofx.Controller

class ConcatenationCanvasController : Controller() {
    private val coroutineScope = CoroutineScope(Dispatchers.Default)
    private val model: ConcatenationCanvasModel by inject()

    private val mainGrinScope: MainGrinScopeWrapper by inject()

    fun replaceAll(
        cartesianSpaces: List<CartesianSpace>,
        arrows: List<Arrow>,
        descriptions: List<Description>
    ){
        model.cartesianSpaces.setAll(cartesianSpaces)
        model.arrows.setAll(arrows)
        model.descriptions.setAll(descriptions)

        model.normalizeSpaces()

        coroutineScope.launch {
            model.reportUpdateAll()
        }
    }

    fun addArrow(arrow: ArrowDTO) {
        model.arrows.add(ArrowConverter.convert(arrow))
    }

    fun openFunctionModal(
        xExistDirection: List<ExistDirection>,
        yExistDirection: List<ExistDirection>
    ) {
        find<AddFunctionModalView>(
            mapOf(
                AddFunctionModalView::xExistDirections to xExistDirection,
                AddFunctionModalView::yExistDirections to yExistDirection
            )
        ).openModal()
    }

    fun openArrowModal(x: Double, y: Double, window: Window?) {
        find<ArrowModalView>(
            mapOf(
                ArrowModalView::type to ConcatenationType,
                ArrowModalView::x to x,
                ArrowModalView::y to y
            )
        ).openModal(stageStyle = StageStyle.UTILITY)
    }

    fun openDescriptionModal(x: Double, y: Double, window: Window? = null) {
        val descriptionChangeModalScope = mainGrinScope.koinScope.get<DescriptionChangeModalScope>()

        val initData = DescriptionModalInitData(x, y)
        val view = descriptionChangeModalScope.get<ChangeDescriptionView> { parametersOf(initData)}

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

    fun clearCanvas() {
        model.clearAll()

        coroutineScope.launch {
            model.reportUpdateAll()
        }
    }
}