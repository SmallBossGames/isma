package ru.nstu.grin.concatenation.canvas.view

import javafx.scene.Parent
import javafx.scene.control.Tooltip
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import ru.nstu.grin.concatenation.canvas.model.ConcatenationViewModel
import ru.nstu.grin.concatenation.canvas.model.EditMode
import tornadofx.Fragment
import tornadofx.action
import tornadofx.button
import tornadofx.toolbar

class ModesPanel : Fragment() {
    private val model: ConcatenationViewModel by inject()

    override val root: Parent = toolbar {
        button {
            val image = Image("view-tool.png")
            val imageView = ImageView(image)
            imageView.fitWidth = 20.0
            imageView.fitHeight = 20.0
            graphic = imageView
            tooltip = Tooltip("Просмотр")

            action {
                model.currentEditMode = EditMode.VIEW
            }
        }
        button {
            val image = Image("select.png")
            val imageView = ImageView(image)
            imageView.fitWidth = 20.0
            imageView.fitHeight = 20.0
            graphic = imageView
            tooltip = Tooltip("Выбор")

            action {
                model.currentEditMode = EditMode.SELECTION
            }
        }
        button {
            val image = Image("move.png")
            val imageView = ImageView(image)
            imageView.fitWidth = 20.0
            imageView.fitHeight = 20.0
            graphic = imageView
            tooltip = Tooltip("Двигать")

            action {
                model.currentEditMode = EditMode.MOVE
            }
        }
        button {
            val image = Image("scale-tool.png")
            val imageView = ImageView(image)
            imageView.fitHeight = 20.0
            imageView.fitWidth = 20.0
            graphic = imageView
            tooltip = Tooltip("Скалирование")

            action {
                model.currentEditMode = EditMode.SCALE
            }
        }
        button {
            val image = Image("edit-tool.png")
            val imageView = ImageView(image)
            imageView.setFitHeight(20.0)
            imageView.setFitWidth(20.0)
            graphic = imageView
            tooltip = Tooltip("Редактирование")

            action {
                model.currentEditMode = EditMode.EDIT
            }
        }
        button {
            val image = Image("window-tool.png")
            val imageView = ImageView(image)
            imageView.fitHeight = 20.0
            imageView.fitWidth = 20.0
            graphic = imageView
            tooltip = Tooltip("Открытие новых окон")

            action {
                model.currentEditMode = EditMode.WINDOWED
            }
        }
    }
}