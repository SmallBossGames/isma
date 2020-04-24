package ru.nstu.grin.concatenation.view.modal

import javafx.beans.property.SimpleStringProperty
import javafx.scene.Parent
import ru.nstu.grin.common.model.WaveletTransformFun
import ru.nstu.grin.concatenation.controller.PointsViewController
import ru.nstu.grin.concatenation.model.AddFunctionsMode
import ru.nstu.grin.concatenation.model.FileReaderMode
import ru.nstu.grin.concatenation.model.PointsViewModel
import tornadofx.*

class PointsView : View() {
    private val controller: PointsViewController by inject(params = params)
    private val model: PointsViewModel by inject(params = params)

    override val root: Parent = form {
        controller.readPoints()
        fieldset("Вейвлет преобразование") {
            field("Включить") {
                checkbox().bind(model.isWaveletProperty)
            }
            field("Тип вейвлета") {
                combobox(model.waveletTransformFunProperty, WaveletTransformFun.values().toList()) {
                    enableWhen {
                        model.isWaveletProperty
                    }
                }
            }
        }
        fieldset("Как добавлять функции") {
            field("Режим") {
                combobox(model.addFunctionsModeProperty, AddFunctionsMode.values().toList()) {
                    cellFormat {
                        text = when (it) {
                            AddFunctionsMode.ADD_TO_ONE_CARTESIAN_SPACE -> "Добавить все в одно пространство"
                            AddFunctionsMode.ADD_TO_NEW_CARTESIAN_SPACES -> "Добавить все в разные новые пространства"
                        }
                    }
                }
            }
        }
        tableview(model.pointsList) {
            items.first().forEachIndexed { index, list ->
                val name = when (model.readerMode) {
                    FileReaderMode.ONE_TO_MANY -> if (index == 0) {
                        "x"
                    } else {
                        "y$index"
                    }
                    FileReaderMode.SEQUENCE -> if (index % 2 == 0) {
                        "x${index / 2}"
                    } else {
                        "y${index / 2}"
                    }
                }
                column(name, String::class) {
                    setCellValueFactory { row ->
                        SimpleStringProperty(row.value[index])
                    }
                }
            }
        }
        button("Ok") {
            action {
                controller.sendFireCheckedEvent()
                close()
            }
        }
    }
}