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
import ru.nstu.grin.concatenation.canvas.model.ConcatenationCanvasModel
import ru.nstu.grin.concatenation.canvas.model.ConcatenationCanvasViewModel
import ru.nstu.grin.concatenation.description.model.DescriptionModalForUpdate
import ru.nstu.grin.concatenation.description.view.ChangeDescriptionView
import ru.nstu.grin.concatenation.function.controller.MirrorFunctionController
import ru.nstu.grin.concatenation.function.service.FunctionOperationsService
import ru.nstu.grin.concatenation.function.view.ChangeFunctionFragment
import ru.nstu.grin.concatenation.function.view.LocalizeFunctionFragment
import ru.nstu.grin.concatenation.koin.DescriptionChangeModalScope
import ru.nstu.grin.concatenation.koin.FunctionChangeModalScope
import ru.nstu.grin.concatenation.koin.MainGrinScope
import tornadofx.find

class TransformToolBar(
    private val mainGrinScope: MainGrinScope,
    private val canvasViewModel: ConcatenationCanvasViewModel,
    private val canvasModel: ConcatenationCanvasModel,
    private val functionOperationsService: FunctionOperationsService,
    private val mirrorFunctionController: MirrorFunctionController,
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
                val initData = DescriptionModalForUpdate(
                    cartesianSpace = canvasModel.cartesianSpaces.first { it.descriptions.contains(description) },
                    description = description,
                )
                val view = descriptionChangeModalScope.get<ChangeDescriptionView> { parametersOf(initData)}

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
            canvasViewModel.selectedFunctions.forEach{
                mirrorFunctionController.toggleMirrorFunction(it, byX = true)
            }
        }
    },
    Button(null, ImageView(Image("mirror-tool.png")).apply {
        fitWidth = 20.0
        fitHeight = 20.0
    }).apply {
        tooltip = Tooltip("Mirror Y")

        setOnAction {
            canvasViewModel.selectedFunctions.forEach{
                mirrorFunctionController.toggleMirrorFunction(it, byY = true)
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
                functionOperationsService.localizeFunction(function)
            }
        }
    },
)