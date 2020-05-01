package ru.nstu.grin.concatenation.function.view

import javafx.scene.Parent
import javafx.scene.control.TabPane
import javafx.scene.layout.Priority
import ru.nstu.grin.concatenation.axis.model.Direction
import ru.nstu.grin.concatenation.canvas.model.ExistDirection
import ru.nstu.grin.concatenation.function.controller.AddFunctionController
import ru.nstu.grin.concatenation.function.model.AddFunctionModel
import ru.nstu.grin.concatenation.function.model.FileFunctionModel
import ru.nstu.grin.concatenation.function.model.InputWay
import ru.nstu.grin.concatenation.points.events.FileCheckedEvent
import tornadofx.*

class AddFunctionModalView : Fragment() {
    val xExistDirections: List<ExistDirection> by param()
    val yExistDirections: List<ExistDirection> by param()
    private val controller: AddFunctionController by inject()
    private val model: AddFunctionModel by inject()
    private val fileFunctionModel: FileFunctionModel by inject()
    private lateinit var tabPane: TabPane

    override val root: Parent = form {
        println(controller.params)
        model.inputWayProperty.onChange {
            when (model.inputWay) {
                InputWay.FILE -> {
                    tabPane.selectionModel.select(0)
                }
                InputWay.ANALYTIC -> {
                    tabPane.selectionModel.select(1)
                }
                InputWay.MANUAL -> {
                    tabPane.selectionModel.select(2)
                }
            }
        }
        fieldset {
            field("Введите имя функции") {
                textfield().bind(model.functionNameProperty)
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
            tabPane = this
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

        fieldset("Ось x") {
            field("Имя") {
                textfield().bind(model.xAxisNameProperty)
            }
            field("Напрвление") {
                val default = Direction.values().map {
                    ExistDirection(
                        it,
                        null
                    )
                }
                val existDirections = xExistDirections
                combobox(model.xDirectionProperty, default + existDirections) {
                    cellFormat {
                        text = if (it.functionName != null) {
                            "Напрвление ${it.direction.name}, функция ${it.functionName}"
                        } else {
                            it.direction.name
                        }
                    }
                }
            }
        }
        fieldset("Ось y") {
            field("Имя") {
                textfield().bind(model.yAxisNameProperty)
            }
            field("Направление") {
                val default = Direction.values().map {
                    ExistDirection(
                        it,
                        null
                    )
                }
                val existDirections = yExistDirections
                combobox(model.yDirectionProperty, default + existDirections) {
                    cellFormat {
                        text = if (it.functionName != null) {
                            "Напрвление ${it.direction.name}, функция ${it.functionName}"
                        } else {
                            it.direction.name
                        }
                    }
                }
            }
        }
        fieldset("Цвета") {
            field("Цвет функций") {
                colorpicker().bind(model.functionColorProperty)
            }
            field("Цвет x оси") {
                colorpicker().bind(model.xAxisColorProperty)
            }
            field("Цвет дельт оси x") {
                colorpicker().bind(model.xDelimeterColorProperty)
            }
            field("Цвет y оси") {
                colorpicker().bind(model.yAxisColorProperty)
            }
            field("Цвте дельт оси y") {
                colorpicker().bind(model.yDelimiterColorProperty)
            }
        }
        button("OK") {
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