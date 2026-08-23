package ru.isma.next.app.views.layout

import javafx.scene.control.TableCell
import javafx.scene.control.TableColumn
import javafx.scene.control.TableView
import javafx.scene.control.cell.PropertyValueFactory
import ru.isma.next.app.models.CompilationErrorItem
import ru.isma.next.app.viewmodels.ErrorListViewModel

class IsmaErrorListTable(
    viewModel: ErrorListViewModel
) : TableView<CompilationErrorItem>(viewModel.errors) {
    init {
        maxHeight = 200.0

        columns.addAll(
            TableColumn<CompilationErrorItem, Int>("Row").apply {
                cellValueFactory = PropertyValueFactory(CompilationErrorItem::row.name)
                setCellFactory { NumericCell() }
                prefWidthProperty().bind(this@IsmaErrorListTable.widthProperty().multiply(0.05))
                isResizable = false
            },
            TableColumn<CompilationErrorItem, Int>("Position").apply {
                cellValueFactory = PropertyValueFactory(CompilationErrorItem::position.name)
                setCellFactory { NumericCell() }
                prefWidthProperty().bind(this@IsmaErrorListTable.widthProperty().multiply(0.05))
                isResizable = false
            },
            TableColumn<CompilationErrorItem, String>("Fragment").apply {
                cellValueFactory = PropertyValueFactory(CompilationErrorItem::fragmentName.name)
                prefWidthProperty().bind(this@IsmaErrorListTable.widthProperty().multiply(0.1))
                isResizable = false
            },
            TableColumn<CompilationErrorItem, String>("Message").apply {
                cellValueFactory = PropertyValueFactory(CompilationErrorItem::message.name)
                prefWidthProperty().bind(this@IsmaErrorListTable.widthProperty().multiply(0.8))
                isResizable = false
            }
        )
    }

    class NumericCell : TableCell<CompilationErrorItem, Int>() {
        override fun updateItem(item: Int?, empty: Boolean) {
            super.updateItem(item, empty)
            text = if (item != null && item >= 0) item.toString() else ""
        }
    }
}
