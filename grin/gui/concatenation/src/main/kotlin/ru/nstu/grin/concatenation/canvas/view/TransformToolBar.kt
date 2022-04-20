package ru.nstu.grin.concatenation.canvas.view

import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.ToolBar
import javafx.scene.control.Tooltip
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.stage.Modality
import javafx.stage.Stage
import org.koin.core.component.get
import org.koin.core.parameter.parametersOf
import ru.nstu.grin.concatenation.canvas.model.ConcatenationCanvasViewModel
import ru.nstu.grin.concatenation.description.view.ChangeDescriptionView
import ru.nstu.grin.concatenation.function.model.UpdateFunctionData
import ru.nstu.grin.concatenation.function.service.FunctionsOperationsService
import ru.nstu.grin.concatenation.function.service.FunctionCanvasService
import ru.nstu.grin.concatenation.function.view.ChangeFunctionFragment
import ru.nstu.grin.concatenation.function.view.LocalizeFunctionFragment
import ru.nstu.grin.concatenation.function.view.MirrorFunctionFragment
import ru.nstu.grin.concatenation.koin.DescriptionChangeModalScope
import ru.nstu.grin.concatenation.koin.FunctionChangeModalScope
import ru.nstu.grin.concatenation.koin.MainGrinScope
import tornadofx.Scope
import tornadofx.find

class TransformToolBar(
    private val scope: Scope,
    private val mainGrinScope: MainGrinScope,
    private val canvasViewModel: ConcatenationCanvasViewModel,
    private val functionCanvasService: FunctionCanvasService,
    private val functionsOperationsService: FunctionsOperationsService,
) : ToolBar(
    Button(null, ImageView(Image("edit-tool.png")).apply {
        fitWidth = 20.0
        fitHeight = 20.0
    }).apply {
        tooltip = Tooltip("Edit")

        setOnAction {
            val function = canvasViewModel.selectedFunctions.firstOrNull()
            if (function != null) {
                val functionChangeModalScope = mainGrinScope.get<FunctionChangeModalScope>()
                val view = functionChangeModalScope.get<ChangeFunctionFragment> { parametersOf(function)}

                Stage().apply {
                    scene = Scene(view)
                    title = "Change Function"
                    initModality(Modality.WINDOW_MODAL)

                    setOnCloseRequest {
                        functionChangeModalScope.closeScope()
                    }

                    show()
                }

                return@setOnAction
            }

            val description = canvasViewModel.selectedDescriptions.firstOrNull()
            if (description != null) {
                val descriptionChangeModalScope = mainGrinScope.get<DescriptionChangeModalScope>()
                val view = descriptionChangeModalScope.get<ChangeDescriptionView> { parametersOf(description)}

                Stage().apply {
                    scene = Scene(view)
                    title = "Change Description"
                    initModality(Modality.WINDOW_MODAL)

                    setOnCloseRequest {
                        descriptionChangeModalScope.closeScope()
                    }

                    show()
                }

                return@setOnAction
            }
        }
    },
    Button(null, ImageView(Image("mirror-tool.png")).apply {
        fitWidth = 20.0
        fitHeight = 20.0
    }).apply {
        tooltip = Tooltip("Mirror X")

        setOnAction {
            val function = canvasViewModel.selectedFunctions.firstOrNull()

            if (function == null) {
                find<MirrorFunctionFragment>(
                    scope,
                    mapOf(MirrorFunctionFragment::isMirrorY to false)
                ).openModal()
            } else {
                val mirrorDetails = function.mirrorDetails
                val updateFunctionData = UpdateFunctionData(
                    function = function,
                    name = function.name,
                    color = function.functionColor,
                    lineType = function.lineType,
                    lineSize = function.lineSize,
                    isHide = function.isHide,
                    mirrorDetails = mirrorDetails.copy(
                        isMirrorX = !mirrorDetails.isMirrorX
                    )
                )

                functionCanvasService.updateFunction(updateFunctionData)
            }
        }
    },
    Button(null, ImageView(Image("mirror-tool.png")).apply {
        fitWidth = 20.0
        fitHeight = 20.0
    }).apply {
        tooltip = Tooltip("Mirror Y")

        setOnAction {
            val function = canvasViewModel.selectedFunctions.firstOrNull()

            if (function == null) {
                find<MirrorFunctionFragment>(
                    scope,
                    mapOf(MirrorFunctionFragment::isMirrorY to true)
                ).openModal()
            } else {
                val mirrorDetails = function.mirrorDetails
                val updateFunctionData = UpdateFunctionData(
                    function = function,
                    name = function.name,
                    color = function.functionColor,
                    lineType = function.lineType,
                    lineSize = function.lineSize,
                    isHide = function.isHide,
                    mirrorDetails = mirrorDetails.copy(
                        isMirrorY = !mirrorDetails.isMirrorY
                    )
                )

                functionCanvasService.updateFunction(updateFunctionData)
            }
        }
    },
    Button(null, ImageView(Image("localize.png")).apply {
        fitWidth = 20.0
        fitHeight = 20.0
    }).apply {
        tooltip = Tooltip("Localize")

        setOnAction {
            val function = canvasViewModel.selectedFunctions.firstOrNull()

            if (function == null) {
                find<LocalizeFunctionFragment>().openModal()
            } else {
                functionsOperationsService.localizeFunction(function)
            }
        }
    },
)