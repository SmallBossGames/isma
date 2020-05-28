package ru.nstu.grin.concatenation.function.view

import javafx.scene.Parent
import javafx.scene.control.TabPane
import javafx.scene.layout.Priority
import javafx.scene.text.Font
import ru.nstu.grin.concatenation.axis.model.Direction
import ru.nstu.grin.concatenation.canvas.model.ExistDirection
import ru.nstu.grin.concatenation.function.controller.AddFunctionController
import ru.nstu.grin.concatenation.function.model.AddFunctionModel
import ru.nstu.grin.concatenation.function.model.FileFunctionModel
import ru.nstu.grin.concatenation.function.model.InputWay
import ru.nstu.grin.concatenation.function.model.LineType
import ru.nstu.grin.concatenation.points.events.FileCheckedEvent
import tornadofx.*

class AddFunctionModalView : Fragment() {
    val xExistDirections: List<ExistDirection> by param()
    val yExistDirections: List<ExistDirection> by param()
    private val controller: AddFunctionController by inject()
    private val model: AddFunctionModel by inject()
    private val fileFunctionModel: FileFunctionModel by inject()

    override val root: Parent = form {
        fieldset {
            field("Введите имя пространства") {
                textfield().bind(model.cartesianSpaceNameProperty)
            }
            field("Введите имя функции") {
                textfield().bind(model.functionNameProperty)
            }
            field("Введите размер линий") {
                textfield().bind(model.functionLineSizeProperty)
            }
            field("Выберите как отображать функцию") {
                combobox(model.functionLineTypeProperty, LineType.values().toList()) {
                    cellFormat {
                        text = when (it) {
                            LineType.POLYNOM -> "Полином"
                            LineType.RECT_FILL_DOTES -> "Прямоугольник заполненные точки"
                            LineType.SEGMENTS -> "Сегменты"
                            LineType.RECT_UNFIL_DOTES -> "Прямоуголник незаполненные точки"
                            LineType.CIRCLE_FILL_DOTES -> "Круг заполненные точки"
                            LineType.CIRCLE_UNFILL_DOTES -> "Круг незаполненные точки"
                        }
                    }
                }
            }
            field("Цвет функций") {
                colorpicker().bind(model.functionColorProperty)
            }
            field("Шаг рисования") {
                textfield().bind(model.stepProperty)
            }
        }
        fieldset {
            field("Выберите способ ввода") {
                combobox(model.inputWayProperty, InputWay.values().toList()) {
                    cellFormat {
                        text = when (it) {
                            InputWay.FILE -> "Файл"
                            InputWay.ANALYTIC -> "Аналитически"
                            InputWay.MANUAL -> "Вручную"
                        }
                    }
                }
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
                field("Напрвление") {
                    combobox(model.xDirectionProperty, listOf(Direction.BOTTOM, Direction.TOP)) {
                        cellFormat {
                            text = it.name
                        }
                    }
                }
                field("расстояние между метками") {
                    textfield().bind(model.xDistanceBetweenMarksProperty)
                }
                field("Размер шрифта меток") {
                    textfield().bind(model.xTextSizeProperty)
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
                    combobox(model.yDirectionProperty, listOf(Direction.LEFT, Direction.RIGHT)) {
                        cellFormat {
                            text = it.name
                        }
                    }
                }
                field("Расстояние между метками") {
                    textfield().bind(model.yDistanceBetweenMarksProperty)
                }
                field("Размер шрифта меток") {
                    textfield().bind(model.yTextSizeProperty)
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
                model.valid
            }

            hgrow = Priority.ALWAYS
            vgrow = Priority.ALWAYS
            action {
                controller.addFunction()
                close()
            }
        }
    }

    init {
        subscribe<FileCheckedEvent> {
            fileFunctionModel.points = it.points
            fileFunctionModel.addFunctionsMode = it.addFunctionsMode
        }
    }
}