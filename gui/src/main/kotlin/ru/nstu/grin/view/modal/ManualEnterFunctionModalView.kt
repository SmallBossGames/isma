package ru.nstu.grin.view.modal

import javafx.collections.FXCollections
import javafx.scene.Parent
import ru.nstu.grin.model.Direction
import ru.nstu.grin.controller.ManualEnterFunctionController
import ru.nstu.grin.view.model.ManualEnterFunctionViewModel
import tornadofx.*

/**
 * @author Konstantin Volivach
 */
class ManualEnterFunctionModalView : View() {

    private val model: ManualEnterFunctionViewModel by inject()
    private val controller: ManualEnterFunctionController by inject()

    override val root: Parent = vbox {
        form {
            fieldset("Введите ниже точки") {
                field("Точки:") {
                    textfield().bind(model.textProperty)
                }
            }
            fieldset("Границы X") {
                field("MinX") {
                    textfield().bind(model.minXProperty)
                }
                field("MaxX") {
                    textfield().bind(model.maxXProperty)
                }
            }
            fieldset("Границы Y") {
                field("MinY") {
                    textfield().bind(model.minYProperty)
                }
                field("MaxY") {
                    textfield().bind(model.maxYProperty)
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
                field("Цвет y оси") {
                    colorpicker().bind(model.yAxisColorProperty)
                }
            }
        }
        button("OK") {
            action {
                val function = controller.parseFunction()
                controller.addFunction(function)
                close()
            }
        }
    }
}