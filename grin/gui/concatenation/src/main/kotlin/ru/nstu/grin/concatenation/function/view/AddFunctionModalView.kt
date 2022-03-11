package ru.nstu.grin.concatenation.function.view

import javafx.collections.FXCollections
import javafx.scene.control.ComboBox
import javafx.scene.control.ListCell
import javafx.scene.layout.Priority
import javafx.scene.text.Font
import javafx.stage.Stage
import ru.isma.javafx.extensions.controls.comboBox
import ru.nstu.grin.concatenation.axis.model.Direction
import ru.nstu.grin.concatenation.function.controller.AddFunctionController
import ru.nstu.grin.concatenation.function.model.*
import tornadofx.*

class AddFunctionModalView(
    private val controller: AddFunctionController,
    private val model: AddFunctionModel,
    private val fileFunctionModel: FileFunctionModel,
    private val manualFunctionModel: ManualFunctionModel,
) : Form() {

    init {
        fieldset {
            field("Введите имя пространства") {
                textfield(model.cartesianSpaceNameProperty).required()
            }
            field("Введите имя функции") {
                textfield(model.functionNameProperty).required()
            }
            field("Введите размер линий") {
                textfield(model.functionLineSizeProperty) {
                    validator {
                        if (it?.toDoubleOrNull() == null) {
                            error("Число должно быть плавающим, например 22,3")
                        } else {
                            null
                        }
                    }
                }
            }
            field("Выберите как отображать функцию") {
                add(
                    comboBox(FXCollections.observableList(LineType.values().toList()), model.functionLineTypeProperty) {
                        object : ListCell<LineType>() {
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
                    }
                )
            }
            field("Цвет функций") {
                colorpicker().bind(model.functionColorProperty)
            }
            field("Шаг рисования") {
                textfield(model.stepProperty) {
                    validator {
                        if (it?.toIntOrNull() == null || (it.toIntOrNull() ?: -1) < 0) {
                            error("Число должно быть целым больше 0")
                        } else {
                            null
                        }
                    }
                }
            }
        }
        fieldset {
            field("Выберите способ ввода") {
                add(
                    comboBox(FXCollections.observableList(InputWay.values().toList()), model.inputWayProperty) {
                        object: ListCell<InputWay>() {
                            override fun updateItem(item: InputWay?, empty: Boolean) {
                                super.updateItem(item, empty)

                                text = when (item) {
                                    InputWay.FILE -> "Файл"
                                    InputWay.ANALYTIC -> "Аналитически"
                                    InputWay.MANUAL -> "Вручную"
                                    else -> null
                                }
                            }
                        }
                    }
                )
            }
        }
        tabpane {
            model.inputWayProperty.onChange {
                when (model.inputWay) {
                    InputWay.FILE -> {
                        selectionModel.select(0)
                    }
                    InputWay.ANALYTIC -> {
                        selectionModel.select(1)
                    }
                    InputWay.MANUAL -> {
                        selectionModel.select(2)
                    }
                }
            }
            when (model.inputWay) {
                InputWay.FILE -> {
                    selectionModel.select(0)
                }
                InputWay.ANALYTIC -> {
                    selectionModel.select(1)
                }
                InputWay.MANUAL -> {
                    selectionModel.select(2)
                }
            }
            tab<FileFunctionFragment>()
            tab<AnalyticFunctionFragment>()
            tab<ManualFunctionFragment>()
            tabMaxHeight = 0.0
            tabMinHeight = 0.0
            stylesheet {
                Stylesheet.tabHeaderArea {
                    visibility = FXVisibility.HIDDEN
                }
            }
        }
        hbox {
            fieldset("Ось x") {
                field("Имя") {
                    textfield().bind(model.xAxisNameProperty)
                }
                field("Направление") {
                    add(
                        ComboBox(FXCollections.observableArrayList(Direction.BOTTOM, Direction.TOP)).apply {
                            valueProperty().bindBidirectional(model.xDirectionProperty)
                        }
                    )
                }
                field("Расстояние между метками") {
                    textfield(model.xDistanceBetweenMarksProperty) {
                        validator {
                            if (it?.toDoubleOrNull() == null || (it.toDoubleOrNull() ?: -1.0) < 0.0) {
                                error("Число должно быть плавающим 20,0 и больше нуля")
                            } else {
                                null
                            }
                        }
                    }
                }
                field("Размер шрифта меток") {
                    textfield(model.xTextSizeProperty) {
                        validator {
                            if (it?.toDoubleOrNull() == null || (it.toDoubleOrNull() ?: -1.0) < 0.0) {
                                error("Число должно быть плавающим 20,0 и больше нуля")
                            } else {
                                null
                            }
                        }
                    }
                }
                field("Шрифт") {
                    combobox(model.xFontProperty, Font.getFamilies())
                }
                field("Цвет x оси") {
                    colorpicker().bind(model.xAxisColorProperty)
                }
                field("Цвет дельт оси x") {
                    colorpicker().bind(model.xDelimeterColorProperty)
                }
            }
            spacing = 20.0
            fieldset("Ось y") {
                field("Имя") {
                    textfield().bind(model.yAxisNameProperty)
                }
                field("Направление") {
                    add(
                        ComboBox(FXCollections.observableArrayList(Direction.LEFT, Direction.RIGHT)).apply {
                            valueProperty().bindBidirectional(model.xDirectionProperty)
                        }
                    )
                }
                field("Расстояние между метками") {
                    textfield(model.yDistanceBetweenMarksProperty) {
                        validator {
                            if (it?.toDoubleOrNull() == null || (it.toDoubleOrNull() ?: -1.0) < 0.0) {
                                error("Число должно быть плавающим 20,0 и больше нуля")
                            } else {
                                null
                            }
                        }
                    }
                }
                field("Размер шрифта меток") {
                    textfield(model.yTextSizeProperty) {
                        validator {
                            if (it?.toDoubleOrNull() == null || (it.toDoubleOrNull() ?: -1.0) < 0.0) {
                                error("Число должно быть плавающим 20,0 и больше нуля")
                            } else {
                                null
                            }
                        }
                    }
                }
                field("Шрифт") {
                    combobox(model.yFontProperty, Font.getFamilies())
                }
                field("Цвет y оси") {
                    colorpicker().bind(model.yAxisColorProperty)
                }
                field("Цвте дельт оси y") {
                    colorpicker().bind(model.yDelimiterColorProperty)
                }
            }
        }
        button("Добавить") {
            enableWhen {
                when (model.inputWay) {
                    InputWay.FILE -> model.valid
                    InputWay.ANALYTIC -> model.valid
                    InputWay.MANUAL -> {
                        model.valid.and(manualFunctionModel.valid)
                    }
                }
            }

            hgrow = Priority.ALWAYS
            vgrow = Priority.ALWAYS
            action {
                controller.addFunction()

                (scene.window as Stage).close()
            }
        }
    }

    init {
/*        subscribe<FileCheckedEvent> {
            fileFunctionModel.points = it.points
            fileFunctionModel.addFunctionsMode = it.addFunctionsMode
        }*/
    }
}