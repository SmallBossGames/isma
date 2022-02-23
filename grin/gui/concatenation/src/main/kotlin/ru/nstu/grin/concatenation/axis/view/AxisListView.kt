package ru.nstu.grin.concatenation.axis.view

import javafx.geometry.Insets
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.control.Label
import javafx.scene.control.Tooltip
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.scene.layout.Background
import javafx.scene.layout.BackgroundFill
import javafx.scene.layout.CornerRadii
import javafx.scene.text.Font
import javafx.stage.Modality
import javafx.stage.Stage
import ru.nstu.grin.concatenation.axis.controller.AxisListViewController
import ru.nstu.grin.concatenation.axis.events.GetAllAxisQuery
import ru.nstu.grin.concatenation.axis.model.AxisListViewModel
import tornadofx.*

class AxisListView : Fragment() {
    private val model: AxisListViewModel by inject()
    private val controller: AxisListViewController = find { }

    override val root: Parent = listview(model.axises) {
        cellFormat {
            graphic = borderpane {
                left {
                    hbox {
                        spacing = 10.0

                        add(Label(it.name).apply {
                            textFill = it.fontColor
                            font = Font(it.font, it.textSize)
                            background = Background(
                                BackgroundFill(
                                    it.backGroundColor,
                                    CornerRadii(0.0),
                                    Insets(0.0)
                                )
                            )
                            padding = Insets(5.0)
                        })

                        add(Label("Direction: ${it.direction}").apply {
                            padding = Insets(5.0)
                        })
                    }
                }

                right {
                    button {
                        action {
                            val view = find<AxisChangeFragment>(
                                mapOf(
                                    AxisChangeFragment::axisId to it.id
                                )
                            )

                            Stage().apply {
                                scene = Scene(view.root)
                                title = "Change axis"
                                initModality(Modality.WINDOW_MODAL)
                                initOwner(currentWindow!!)

                                show()
                            }
                        }
                        val image = Image("edit-tool.png")
                        val imageView = ImageView(image)
                        imageView.fitHeight = 20.0
                        imageView.fitWidth = 20.0
                        graphic = imageView
                        tooltip = Tooltip("Отредактировать")
                    }
                }
            }
        }
    }

    init {
        fire(GetAllAxisQuery())
    }
}