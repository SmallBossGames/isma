package ru.nstu.grin.concatenation.points.view

import javafx.beans.property.SimpleStringProperty
import javafx.scene.Parent
import ru.nstu.grin.concatenation.points.controller.PointsViewController
import ru.nstu.grin.concatenation.points.model.AddFunctionsMode
import ru.nstu.grin.concatenation.file.options.model.FileReaderMode
import ru.nstu.grin.concatenation.points.model.PointsModel
import ru.nstu.grin.concatenation.function.model.FileModel
import tornadofx.*

class PointsView : Fragment() {
    private val model = PointsModel()
    private val fileModel = FileModel()
    private val controller = PointsViewController(model, fileModel)

    init {
        controller.readPoints()
    }

    override val root: Parent = form {
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
        button("Подвердить") {
            action {
                controller.sendFireCheckedEvent()
                close()
            }
        }
    }
}
