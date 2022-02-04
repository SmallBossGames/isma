package ru.nstu.grin.concatenation.cartesian.view

import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.control.Label
import javafx.scene.control.Tooltip
import javafx.scene.image.Image
import javafx.scene.image.ImageView
import javafx.stage.Modality
import javafx.stage.Stage
import ru.nstu.grin.concatenation.cartesian.controller.CartesianListViewController
import ru.nstu.grin.concatenation.cartesian.events.GetAllCartesiansQuery
import ru.nstu.grin.concatenation.cartesian.model.CartesianListViewModel
import tornadofx.*

class CartesianListView : Fragment() {
    private val model: CartesianListViewModel by inject()
    private val controller: CartesianListViewController = find { }

    override val root: Parent = listview(model.cartesianSpaces) {
        cellFormat {
            graphic = borderpane {
                left {
                    hbox {
                        spacing = 10.0

                        add(Label(it.name))
                        add(Label("Grid: ${ if (it.isShowGrid) "Yes" else "No" }"))
                    }
                }
                right {
                    hbox {
                        spacing = 5.0
                        button {
                            action {
                                val view = find<CopyCartesianFragment>(
                                    mapOf(
                                        CopyCartesianFragment::cartesianId to it.id
                                    )
                                )

                                Stage().apply {
                                    scene = Scene(view.root)
                                    title = "Function parameters"
                                    initModality(Modality.WINDOW_MODAL)
                                    initOwner(currentWindow)

                                    show()
                                }
                            }
                            val image = Image("copy.png")
                            val imageView = ImageView(image)
                            imageView.fitHeight = 20.0
                            imageView.fitWidth = 20.0
                            graphic = imageView
                            tooltip = Tooltip("Скопировать")
                        }
                        button {
                            action {
                                val view = find<ChangeCartesianFragment>(
                                    mapOf(
                                        ChangeCartesianFragment::cartesianId to it.id
                                    )
                                )

                                Stage().apply {
                                    scene = Scene(view.root)
                                    title = "Function parameters"
                                    initModality(Modality.WINDOW_MODAL)
                                    initOwner(currentWindow)

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
                        button {
                            action {
                                controller.deleteCartesian(it.id)
                            }
                            val image = Image("send-to-trash.png")
                            val imageView = ImageView(image)
                            imageView.fitHeight = 20.0
                            imageView.fitWidth = 20.0
                            graphic = imageView
                            tooltip = Tooltip("Удалить")
                        }
                    }
                }
            }
        }
    }

    init {
        fire(GetAllCartesiansQuery())
    }
}