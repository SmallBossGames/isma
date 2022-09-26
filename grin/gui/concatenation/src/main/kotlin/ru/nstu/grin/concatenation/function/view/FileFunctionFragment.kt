package ru.nstu.grin.concatenation.function.view

import javafx.scene.control.Button
import javafx.scene.layout.VBox
import ru.nstu.grin.concatenation.function.controller.FileFragmentController

class FileFunctionFragment(
    private val controller: FileFragmentController
) : VBox(
    Button("Choose File").apply {
        setOnAction {
            controller.chooseFile()
        }
    }
)