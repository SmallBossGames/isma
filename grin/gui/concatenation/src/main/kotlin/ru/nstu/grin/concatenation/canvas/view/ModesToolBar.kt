package ru.nstu.grin.concatenation.canvas.view

import javafx.scene.control.Button
import javafx.scene.control.ToolBar
import javafx.scene.control.Tooltip
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import ru.nstu.grin.concatenation.canvas.model.ConcatenationViewModel
import ru.nstu.grin.concatenation.canvas.model.EditMode

class ModesToolBar(
    private val model: ConcatenationViewModel
): ToolBar(
    Button(null, ImageView(Image("view-tool.png")).apply {
        fitWidth = 20.0
        fitHeight = 20.0
    }).apply {
        tooltip = Tooltip("View")

        setOnAction {
            model.currentEditMode = EditMode.VIEW
        }
    },
    Button(null, ImageView(Image("select.png")).apply {
        fitWidth = 20.0
        fitHeight = 20.0
    }).apply {
        tooltip = Tooltip("Select")

        setOnAction {
            model.currentEditMode = EditMode.SELECTION
        }
    },
    Button(null, ImageView(Image("move.png")).apply {
        fitWidth = 20.0
        fitHeight = 20.0
    }).apply {
        tooltip = Tooltip("Move")

        setOnAction {
            model.currentEditMode = EditMode.MOVE
        }
    },
    Button(null, ImageView(Image("scale-tool.png")).apply {
        fitWidth = 20.0
        fitHeight = 20.0
    }).apply {
        tooltip = Tooltip("Scale")

        setOnAction {
            model.currentEditMode = EditMode.SCALE
        }
    },
    Button(null, ImageView(Image("edit-tool.png")).apply {
        fitWidth = 20.0
        fitHeight = 20.0
    }).apply {
        tooltip = Tooltip("Edit")

        setOnAction {
            model.currentEditMode = EditMode.EDIT
        }
    },
    Button(null, ImageView(Image("window-tool.png")).apply {
        fitWidth = 20.0
        fitHeight = 20.0
    }).apply {
        tooltip = Tooltip("Open new windows")

        setOnAction {
            model.currentEditMode = EditMode.WINDOWED
        }
    },
)