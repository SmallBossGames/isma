package ru.nstu.grin.concatenation.canvas.view

import javafx.scene.Parent
import javafx.scene.control.Tooltip
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import ru.nstu.grin.concatenation.function.view.LocalizeFunctionFragment
import tornadofx.Fragment
import tornadofx.action
import tornadofx.button
import tornadofx.toolbar

class TransformPanel : Fragment() {
    override val root: Parent = toolbar {
        button {
            val image = Image("mirror-tool.png")
            val imageView = ImageView(image)
            imageView.fitWidth = 20.0
            imageView.fitHeight = 20.0
            graphic = imageView
            tooltip = Tooltip("Отзеркалировать")

            action {

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
                find<LocalizeFunctionFragment>().openModal()
            }
        }
    }
}