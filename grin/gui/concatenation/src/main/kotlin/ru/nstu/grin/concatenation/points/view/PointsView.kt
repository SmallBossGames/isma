package ru.nstu.grin.concatenation.points.view

import javafx.collections.FXCollections
import javafx.geometry.Insets
import javafx.scene.Scene
import javafx.scene.control.Button
import javafx.scene.control.ComboBox
import javafx.scene.control.Label
import javafx.scene.control.ListCell
import javafx.scene.control.TableColumn
import javafx.scene.control.TableView
import javafx.scene.layout.VBox
import javafx.stage.Modality
import javafx.stage.Stage
import javafx.stage.Window
import org.koin.core.scope.Scope
import ru.nstu.grin.concatenation.file.options.model.FileReaderMode
import ru.nstu.grin.concatenation.function.model.FileModel
import ru.nstu.grin.concatenation.points.controller.PointsViewController
import ru.nstu.grin.concatenation.points.model.AddFunctionsMode
import ru.nstu.grin.concatenation.points.model.PointsModel

class PointsView(
    private val controller: PointsViewController,
    private val model: PointsModel,
    private val fileModel: FileModel,
) : VBox() {

    init {
        controller.readPoints()
        styleClass.add("points-view")
        padding = Insets(10.0)
        spacing = 10.0

        // Mode selector
        val modeLabel = Label("Как добавлять функции")
        val modeCombo = ComboBox<AddFunctionsMode>(
            FXCollections.observableArrayList(AddFunctionsMode.values().toList())
        ).apply {
            // Bidirectional binding
            valueProperty().addListener { _, _, newValue ->
                model.addFunctionsModeProperty.value = newValue
            }
            model.addFunctionsModeProperty.addListener { _, _, _ ->
                if (value != model.addFunctionsMode) {
                    value = model.addFunctionsMode
                }
            }
            cellFactory = {
                object : ListCell<AddFunctionsMode>() {
                    override fun updateItem(item: AddFunctionsMode?, empty: Boolean) {
                        super.updateItem(item, empty)
                        text = if (item == null) ""
                        else when (item) {
                            AddFunctionsMode.ADD_TO_ONE_CARTESIAN_SPACE -> "Добавить все в одно пространство"
                            AddFunctionsMode.ADD_TO_NEW_CARTESIAN_SPACES -> "Добавить все в разные новые пространства"
                            else -> ""
                        }
                    }
                }
            }
        }

        // Points table
        val table = TableView<List<String>>()
        table.items = model.pointsList

        // Add columns based on reader mode
        model.pointsList.firstOrNull()?.forEachIndexed { index, list ->
            val name = when (fileModel.readerMode) {
                FileReaderMode.ONE_TO_MANY -> if (index == 0) "x" else "y" + index
                FileReaderMode.SEQUENCE -> if (index % 2 == 0) "x" + (index / 2) else "y" + (index / 2)
            }
            val col = TableColumn<List<String>, String>(name)
            col.setCellValueFactory { cellData ->
                javafx.beans.property.SimpleStringProperty(cellData.value[index])
            }
            table.columns.add(col)
        }

        // Confirm button
        val confirmButton = Button("Подвердить").apply {
            setOnAction {
                controller.sendFireCheckedEvent()
                val stage = scene?.window as? Stage
                stage?.close()
            }
        }

        children.setAll(modeLabel, modeCombo, table, confirmButton)
    }

    companion object {
        fun openModal(koinScope: Scope, owner: Window? = null) {
            val view = koinScope.get<PointsView>()
            Stage().apply {
                scene = Scene(view)
                title = "Выбор точек"
                initModality(Modality.WINDOW_MODAL)
                if (owner != null) initOwner(owner)
                showAndWait()
            }
        }
    }
}
