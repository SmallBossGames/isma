package ru.isma.next.app.views.dialogs

import javafx.beans.property.BooleanProperty
import javafx.beans.property.SimpleBooleanProperty
import javafx.collections.FXCollections
import javafx.scene.control.*
import javafx.scene.layout.HBox

private const val LIST_ITEMS_SPACING = 10.0

class NamedPickerItem<T>(val name: String, val value: T)

fun <T> pickItems(item: Iterable<NamedPickerItem<T>>): List<NamedPickerItem<T>> {
    class SelectionItemViewModel(val isSelected: BooleanProperty, val value: NamedPickerItem<T>)

    val selectionItemViewModels = item.map { SelectionItemViewModel(SimpleBooleanProperty(), it) }

    Dialog<ButtonType>().apply {
        title = "Select variables"
        dialogPane.content = ScrollPane(
            ListView(FXCollections.observableArrayList(selectionItemViewModels)).apply {
                setCellFactory {
                    object : ListCell<SelectionItemViewModel>() {
                        override fun updateItem(item: SelectionItemViewModel?, empty: Boolean) {
                            if (empty || item == null || graphic != null) {
                                return
                            }

                            graphic = HBox(
                                CheckBox().apply {
                                    selectedProperty().bindBidirectional(item.isSelected)
                                },
                                Label(item.value.name)
                            ).apply {
                                spacing = LIST_ITEMS_SPACING
                            }
                        }
                    }
                }
            })

        dialogPane.buttonTypes.addAll(
            ButtonType("Ok", ButtonBar.ButtonData.OK_DONE),
            ButtonType("Close", ButtonBar.ButtonData.CANCEL_CLOSE),
        )

        val result = showAndWait()

        return if (result.isPresent && result.get().buttonData == ButtonBar.ButtonData.OK_DONE) {
            selectionItemViewModels.filter { it.isSelected.value }.map { it.value }
        } else {
            emptyList()
        }
    }
}