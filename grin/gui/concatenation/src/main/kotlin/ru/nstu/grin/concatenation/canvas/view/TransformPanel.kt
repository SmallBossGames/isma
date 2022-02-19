package ru.nstu.grin.concatenation.canvas.view

import javafx.scene.Parent
import javafx.scene.control.Tooltip
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import ru.nstu.grin.concatenation.canvas.model.ConcatenationCanvasModel
import ru.nstu.grin.concatenation.description.view.ChangeDescriptionFragment
import ru.nstu.grin.concatenation.function.events.LocalizeFunctionEvent
import ru.nstu.grin.concatenation.function.events.UpdateFunctionEvent
import ru.nstu.grin.concatenation.function.view.ChangeFunctionFragment
import ru.nstu.grin.concatenation.function.view.LocalizeFunctionFragment
import ru.nstu.grin.concatenation.function.view.MirrorFunctionFragment
import tornadofx.*

class TransformPanel : Fragment() {
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
                        ChangeFunctionFragment::functionId to function.id
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
                    val mirrorEvent = UpdateFunctionEvent(
                        id = function.id,
                        name = function.name,
                        color = function.functionColor,
                        lineType = function.lineType,
                        lineSize = function.lineSize,
                        isHide = function.isHide,
                        mirroDetails = mirrorDetails.copy(
                            isMirrorX = !mirrorDetails.isMirrorX
                        )
                    )
                    fire(mirrorEvent)
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
                    val mirrorEvent = UpdateFunctionEvent(
                        id = function.id,
                        name = function.name,
                        color = function.functionColor,
                        lineType = function.lineType,
                        lineSize = function.lineSize,
                        isHide = function.isHide,
                        mirroDetails = mirrorDetails.copy(
                            isMirrorY = !mirrorDetails.isMirrorY
                        )
                    )
                    fire(mirrorEvent)
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
                    val event = LocalizeFunctionEvent(id = function.id)
                    fire(event)
                }
            }
        }
    }
}