package ru.nstu.grin.view

import javafx.collections.FXCollections
import javafx.scene.Parent
import ru.nstu.grin.Direction
import ru.nstu.grin.controller.ManualEnterFunctionController
import ru.nstu.grin.model.ManualEnterFunctionModel
import tornadofx.*

/**
 * @author Konstantin Volivach
 */
class ManualEnterFunctionView : View() {

    private val model: ManualEnterFunctionModel by inject()
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
                    combobox<String>(model.xDirectionProperty, directions)
                }
            }
            fieldset("Цвета") {
                field("Цвет функций") {
                    textfield().bind(model.functionColorProperty)
                }
                field("Цвет x оси") {
                    textfield().bind(model.xAxisColorProperty)
                }
                field("Цвет y оси") {
                    textfield().bind(model.yAxisColorProperty)
                }
            }
        }
        button("OK") {
            action {
                val function = controller.parseFunction()
                controller.addFunction(function)
                closeModal()
            }
        }
    }
}