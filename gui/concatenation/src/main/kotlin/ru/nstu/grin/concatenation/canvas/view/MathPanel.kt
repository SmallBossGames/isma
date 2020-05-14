package ru.nstu.grin.concatenation.canvas.view

import javafx.scene.Parent
import javafx.scene.control.Tooltip
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import ru.nstu.grin.concatenation.function.view.IntersectionFunctionFragment
import tornadofx.Fragment
import tornadofx.action
import tornadofx.button
import tornadofx.toolbar

class MathPanel : Fragment() {
    override val root: Parent = toolbar {
        button {
            val image = Image("intersection.png")
            val imageView = ImageView(image)
            imageView.fitHeight = 20.0
            imageView.fitWidth = 20.0
            graphic = imageView
            tooltip = Tooltip("Найти пересечения")

            action {
                find<IntersectionFunctionFragment>().openModal()
            }
        }
    }
}