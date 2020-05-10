package ru.nstu.grin.concatenation.points.view

import javafx.beans.property.SimpleStringProperty
import javafx.scene.Parent
import ru.nstu.grin.common.model.WaveletTransformFun
import ru.nstu.grin.concatenation.points.controller.PointsViewController
import ru.nstu.grin.concatenation.points.model.AddFunctionsMode
import ru.nstu.grin.concatenation.file.options.model.FileReaderMode
import ru.nstu.grin.concatenation.points.model.PointsViewModel
import ru.nstu.grin.common.model.WaveletDirection
import ru.nstu.grin.concatenation.function.model.FileModel
import tornadofx.*

class PointsView : Fragment() {
    private val controller: PointsViewController by inject()
    private val model: PointsViewModel by inject()
    private val fileModel: FileModel by inject()

    init {
        controller.readPoints()
    }

    override val root: Parent = form {
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
            field("Какую ось преобразовывать") {
                combobox(model.waveletDirectionProperty, WaveletDirection.values().toList()) {
                    cellFormat {
                        text = when (it) {
                            WaveletDirection.X -> {
                                "Ось абсцисс"
                            }
                            WaveletDirection.Y -> {
                                "Ось ординат"
                            }
                            WaveletDirection.BOTH -> {
                                "Обе оси"
                            }
                        }
                    }
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
                val name = when (fileModel.readerMode) {
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