package ru.isma.next.app.views.toolbars

import javafx.scene.control.TableCell
import javafx.scene.control.TableColumn
import javafx.scene.control.TableView
import javafx.scene.control.cell.PropertyValueFactory
import ru.isma.next.app.models.ErrorViewModel
import ru.isma.next.app.services.ModelErrorService


class IsmaErrorListTable(
    modelErrorService: ModelErrorService
) : TableView<ErrorViewModel>(modelErrorService.errors) {
    init {
        maxHeight = 200.0

        columns.addAll(
            TableColumn<ErrorViewModel, Int>("Row").apply {
                cellValueFactory = PropertyValueFactory(ErrorViewModel::row.name)
                setCellFactory { NumericCell() }
                prefWidthProperty().bind(this@IsmaErrorListTable.widthProperty().multiply(0.05))
                isResizable = false
            },
            TableColumn<ErrorViewModel, Int>("Position").apply {
                cellValueFactory = PropertyValueFactory(ErrorViewModel::position.name)
                setCellFactory { NumericCell() }
                prefWidthProperty().bind(this@IsmaErrorListTable.widthProperty().multiply(0.05))
                isResizable = false
            },
            TableColumn<ErrorViewModel, Int>("Fragment").apply {
                cellValueFactory = PropertyValueFactory(ErrorViewModel::fragmentName.name)
                prefWidthProperty().bind(this@IsmaErrorListTable.widthProperty().multiply(0.1))
                isResizable = false
            },
            TableColumn<ErrorViewModel, Int>("Message").apply {
                cellValueFactory = PropertyValueFactory(ErrorViewModel::message.name)
                prefWidthProperty().bind(this@IsmaErrorListTable.widthProperty().multiply(0.8))
                isResizable = false
            }
        )
    }

    class NumericCell : TableCell<ErrorViewModel, Int>() {
        override fun updateItem(item: Int?, empty: Boolean) {
            text = if (item != null && item >= 0) item.toString() else ""
        }
    }
}