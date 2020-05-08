package ru.nstu.grin.concatenation.function.view

import javafx.scene.Parent
import tornadofx.Fragment
import tornadofx.vbox
import java.util.*

class ChangeFunctionFragment : Fragment() {
    val functionId: UUID by param()

    override val root: Parent = vbox {

    }
}