package ru.nstu.grin.concatenation.canvas.view

import javafx.scene.Parent
import javafx.scene.control.Tooltip
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import ru.nstu.grin.concatenation.canvas.model.ConcatenationCanvasModel
import ru.nstu.grin.concatenation.description.view.ChangeDescriptionFragment
import ru.nstu.grin.concatenation.function.model.UpdateFunctionData
import ru.nstu.grin.concatenation.function.service.FunctionCanvasService
import ru.nstu.grin.concatenation.function.view.ChangeFunctionFragment
import ru.nstu.grin.concatenation.function.view.LocalizeFunctionFragment
import ru.nstu.grin.concatenation.function.view.MirrorFunctionFragment
import tornadofx.*

class TransformPanel : Fragment() {
    private val functionCanvasService: FunctionCanvasService by inject()
    private val model: ConcatenationCanvasModel by inject()

    override val root: Parent = toolbar {
        button {
            val image = Image("edit-tool.png")
            val imageView = ImageView(image)
            imageView.fitWidth = 20.0
            imageView.fitHeight = 20.0
            graphic = imageView
            tooltip = Tooltip("Отредактировать")

            action {
                val function = model.getSelectedFunction()
                if (function != null) {
                    find<ChangeFunctionFragment>(
                        "function" to function
                    ).openModal()
                    return@action
                }
                val description = model.getSelectedDescription()
                if (description != null) {
                    find<ChangeDescriptionFragment>(
                        ChangeDescriptionFragment::descriptionId to description.id
                    ).openModal()
                    return@action
                }
                enableWhen {
                    val selectedFunction = model.getSelectedFunction()
                    val selectedDescription = model.getSelectedDescription()
                    (selectedFunction != null || selectedDescription != null).toProperty()
                }
            }
        }
        button {
            val image = Image("mirror-tool.png")
            val imageView = ImageView(image)
            imageView.fitWidth = 20.0
            imageView.fitHeight = 20.0
            graphic = imageView
            tooltip = Tooltip("Отзеркалировать по X")

            action {
                val function = model.getSelectedFunction()

                if (function == null) {
                    find<MirrorFunctionFragment>(
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
        }
        button {
            val image = Image("mirror-tool.png")
            val imageView = ImageView(image)
            imageView.fitWidth = 20.0
            imageView.fitHeight = 20.0
            graphic = imageView
            tooltip = Tooltip("Отзеркалировать по Y")

            action {
                val function = model.getSelectedFunction()

                if (function == null) {
                    find<MirrorFunctionFragment>(
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
        }
        button {
            val image = Image("localize.png")
            val imageView = ImageView(image)
            imageView.fitWidth = 20.0
            imageView.fitHeight = 20.0
            graphic = imageView
            tooltip = Tooltip("Локализовать")

            action {
                val function = model.getSelectedFunction()
                if (function == null) {
                    find<LocalizeFunctionFragment>().openModal()
                } else {
                    functionCanvasService.localizeFunction(function)
                }
            }
        }
    }
}