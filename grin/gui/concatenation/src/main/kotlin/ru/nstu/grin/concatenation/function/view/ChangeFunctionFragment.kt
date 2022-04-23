package ru.nstu.grin.concatenation.function.view

import javafx.collections.FXCollections
import javafx.geometry.Insets
import javafx.geometry.Side
import javafx.scene.control.*
import javafx.scene.layout.BorderPane
import javafx.scene.layout.HBox
import javafx.scene.layout.Priority
import javafx.scene.layout.VBox
import javafx.stage.Stage
import ru.isma.javafx.extensions.controls.PropertiesGrid
import ru.isma.javafx.extensions.controls.propertiesGrid
import ru.nstu.grin.common.model.WaveletDirection
import ru.nstu.grin.common.model.WaveletTransformFun
import ru.nstu.grin.concatenation.function.controller.ChangeFunctionController
import ru.nstu.grin.concatenation.function.model.*
import ru.nstu.grin.concatenation.function.transform.*

class ChangeFunctionFragment(
    private val model: ChangeFunctionViewModel,
    private val controller: ChangeFunctionController,
) : BorderPane() {
    init {
        val types = FXCollections.observableArrayList(LineType.values().toList())

        center = TabPane(
            Tab("General",
                propertiesGrid {
                    addNode("Name", model.nameProperty)
                    addNode("Color", model.functionColorProperty)
                    addNode("Thickness", model.lineSizeProperty)
                    addNode("Hide", model.isHideProperty)
                    addNode("Line Type", types, model.lineTypeProperty) {
                        createCell()
                    }
                }.apply {
                    padding = Insets(10.0)
                }
            ),
            Tab("Modifiers",
                BorderPane().apply {
                    val propertyGridContainer = VBox().apply {
                        minWidth = 300.0
                    }

                    var selectedItem: IAsyncPointsTransformerViewModel? = null

                    val addContextMenu = ContextMenu(
                        MenuItem(MirrorPointsTransformerViewModel.title).apply {
                            setOnAction {
                                model.addModifier(::MirrorPointsTransformerViewModel)
                            }
                        },
                        MenuItem(LogPointsTransformerViewModel.title).apply {
                            setOnAction {
                                model.addModifier(::LogPointsTransformerViewModel)
                            }
                        },
                        MenuItem(TranslateTransformerViewModel.title).apply {
                            setOnAction {
                                model.addModifier(::TranslateTransformerViewModel)
                            }
                        },
                        MenuItem(WaveletTransformerViewModel.title).apply {
                            setOnAction {
                                model.addModifier(::WaveletTransformerViewModel)
                            }
                        },
                        MenuItem(DerivativeTransformerViewModel.title).apply {
                            setOnAction {
                                model.addModifier(::DerivativeTransformerViewModel)
                            }
                        },
                        MenuItem(IntegratorTransformerViewModel.title).apply {
                            setOnAction {
                                model.addModifier(::IntegratorTransformerViewModel)
                            }
                        }
                    )

                    right = VBox(
                        ToolBar(
                            Button("Add").apply {
                                setOnAction {
                                    addContextMenu.show(this, Side.BOTTOM, 0.0, 0.0)
                                }
                            },
                            Button("Remove").apply {
                                setOnAction {
                                    selectedItem?.let { model.removeModifier(it) }
                                }
                            },
                        ),
                        ListView(model.modifiers).apply {
                            setCellFactory {
                                object : ListCell<IAsyncPointsTransformerViewModel>() {
                                    override fun updateItem(item: IAsyncPointsTransformerViewModel?, empty: Boolean) {
                                        super.updateItem(item, empty)

                                        text = when(item){
                                            is MirrorPointsTransformerViewModel -> "Mirror"
                                            is LogPointsTransformerViewModel -> "Logarithm"
                                            is TranslateTransformerViewModel -> TranslateTransformerViewModel.title
                                            is WaveletTransformerViewModel -> WaveletTransformerViewModel.title
                                            is DerivativeTransformerViewModel -> DerivativeTransformerViewModel.title
                                            is IntegratorTransformerViewModel -> IntegratorTransformerViewModel.title
                                            null -> null
                                        }
                                    }
                                }
                            }
                            selectionModel.selectedItemProperty().addListener{_, _, item: IAsyncPointsTransformerViewModel? ->
                                if(item != null){
                                    propertyGridContainer.children.setAll(propertyGridContainerContent(item))
                                } else{
                                    propertyGridContainer.children.clear()
                                }

                                selectedItem = item
                            }

                            VBox.setVgrow(this, Priority.ALWAYS)
                        }
                    )

                    left = propertyGridContainer
                }
            )
        ).apply {
            tabClosingPolicy = TabPane.TabClosingPolicy.UNAVAILABLE
        }

        bottom = HBox(
            Button("Save").apply {
                setOnAction {
                    controller.updateFunction(model)
                    (scene.window as Stage).close()
                }
            }
        ).apply {
            padding = Insets(10.0)
        }
    }

    companion object {
        @JvmStatic
        private fun createCell() = object : ListCell<LineType>() {
            override fun updateItem(item: LineType?, empty: Boolean) {
                super.updateItem(item, empty)

                text = when (item) {
                    LineType.POLYNOM -> "Полином"
                    LineType.RECT_FILL_DOTES -> "Прямоугольник заполненные точки"
                    LineType.SEGMENTS -> "Сегменты"
                    LineType.RECT_UNFIL_DOTES -> "Прямоуголник незаполненные точки"
                    LineType.CIRCLE_FILL_DOTES -> "Круг заполненные точки"
                    LineType.CIRCLE_UNFILL_DOTES -> "Круг незаполненные точки"
                    else -> null
                }
            }
        }

        fun propertyGridContainerContent(item: IAsyncPointsTransformerViewModel): PropertiesGrid = when(item){
            is MirrorPointsTransformerViewModel -> propertyGridContainerContent(item)
            is LogPointsTransformerViewModel -> propertyGridContainerContent(item)
            is TranslateTransformerViewModel -> propertyGridContainerContent(item)
            is WaveletTransformerViewModel -> propertyGridContainerContent(item)
            is DerivativeTransformerViewModel -> propertyGridContainerContent(item)
            is IntegratorTransformerViewModel -> propertyGridContainerContent(item)
        }.apply {
            padding = Insets(5.0)
        }

        fun propertyGridContainerContent(item: WaveletTransformerViewModel) =
            propertiesGrid {
                addNode("Wavelet Function", FXCollections.observableList(WaveletTransformFun.values().asList()), item.waveletTransformFunProperty)
                addNode("Wavelet Direction", FXCollections.observableList(WaveletDirection.values().asList()), item.waveletDirectionProperty)
            }

        fun propertyGridContainerContent(item: TranslateTransformerViewModel) =
            propertiesGrid {
                addNode("Translate X", item.translateXProperty)
                addNode("Translate Y", item.translateYProperty)
            }

        fun propertyGridContainerContent(item: LogPointsTransformerViewModel) =
            propertiesGrid {
                addNode("X Logarithm", item.isXLogarithmProperty)
                addNode("X Logarithm Base", item.xLogarithmBaseProperty)
                addNode("Y Logarithm", item.isYLogarithmProperty)
                addNode("Y Logarithm Base", item.yLogarithmBaseProperty)
            }

        fun propertyGridContainerContent(item: MirrorPointsTransformerViewModel) =
            propertiesGrid {
                addNode("Mirror X", item.mirrorXProperty)
                addNode("Mirror Y", item.mirrorYProperty)
            }

        fun propertyGridContainerContent(item: DerivativeTransformerViewModel) =
            propertiesGrid {
                addNode("Degree", item.degreeProperty)
                addNode("Type", FXCollections.observableList(DerivativeType.values().asList()), item.typeProperty)
                addNode("Axis", FXCollections.observableList(DerivativeAxis.values().asList()), item.axisProperty)
            }

        fun propertyGridContainerContent(item: IntegratorTransformerViewModel) =
            propertiesGrid {
                addNode("Degree", item.initialValueProperty)
                addNode("Type", FXCollections.observableList(IntegrationMethod.values().asList()), item.methodProperty)
                addNode("Axis", FXCollections.observableList(IntegrationAxis.values().asList()), item.axisProperty)
            }
    }

}