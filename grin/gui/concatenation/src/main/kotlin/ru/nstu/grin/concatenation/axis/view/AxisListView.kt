package ru.nstu.grin.concatenation.axis.view

import javafx.geometry.Insets
import javafx.scene.Parent
import javafx.scene.control.*
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.layout.*
import javafx.scene.text.Font
import ru.nstu.grin.concatenation.axis.controller.AxisListViewController
import ru.nstu.grin.concatenation.axis.model.AxisListViewModel
import ru.nstu.grin.concatenation.axis.model.ConcatenationAxis
import tornadofx.Fragment

class AxisListView(
    viewModel: AxisListViewModel,
    private val controller: AxisListViewController
) : Fragment() {

    override val root: Parent = ListView(viewModel.axes).apply {
        setCellFactory {
            object : ListCell<ConcatenationAxis>() {
                override fun updateItem(item: ConcatenationAxis?, empty: Boolean) {
                    super.updateItem(item, empty)

                    graphic = if (item == null) null else createItem(item)
                }
            }
        }
    }

    private fun createItem(item: ConcatenationAxis): BorderPane {
        return BorderPane().apply {
            left = HBox(
                Label(item.name).apply {
                    textFill = item.fontColor
                    font = Font(item.font, item.textSize)
                    background = Background(
                        BackgroundFill(
                            item.backGroundColor,
                            CornerRadii(0.0),
                            Insets(0.0)
                        )
                    )
                    padding = Insets(5.0)
                },
                Label("Direction: ${item.direction}").apply {
                    padding = Insets(5.0)
                }
            ).apply {
                spacing = 10.0
            }

            right = Button(null, ImageView(Image("edit-tool.png")).apply {
                fitHeight = 20.0
                fitWidth = 20.0
            }).apply {
                tooltip = Tooltip("Отредактировать")
                setOnAction {
                    controller.editAxis(item, currentWindow)
                }
            }
        }
    }
}