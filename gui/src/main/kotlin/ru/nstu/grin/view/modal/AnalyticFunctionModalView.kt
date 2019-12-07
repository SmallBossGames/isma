package ru.nstu.grin.view.modal

import javafx.collections.FXCollections
import javafx.scene.Parent
import javafx.scene.layout.Priority
import ru.nstu.grin.model.Direction
import ru.nstu.grin.controller.AnalyticFunctionController
import ru.nstu.grin.model.DrawSize
import ru.nstu.grin.model.view.AnalyticFunctionModel
import tornadofx.*

/**
 * @author Konstantin Volivach
 */
class AnalyticFunctionModalView : View() {
    val drawSize: DrawSize by param()

    private val model: AnalyticFunctionModel by inject()

    private val controller: AnalyticFunctionController by inject()

    override val root: Parent = form {
        fieldset("Введите формулу") {
            field("формула") {
                textfield().bind(model.textProperty)
            }
            field("Delta") {
                textfield().bind(model.deltaProperty)
            }
        }
        fieldset("Направления осей") {
            field("Ось x") {
                val directions = FXCollections.observableArrayList(
                    Direction.values().map { it.name }
                )
                combobox<String>(model.xDirectionProperty, directions)
            }
            field("Ось y") {
                val directions = FXCollections.observableArrayList(
                    Direction.values().map { it.name }
                )
                combobox<String>(model.yDirectionProperty, directions)
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
                controller.addFunction(drawSize)
                close()
            }
        }
    }
}