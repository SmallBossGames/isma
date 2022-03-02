package ru.isma.next.app.views.dialogs

import javafx.beans.property.BooleanProperty
import javafx.beans.property.SimpleBooleanProperty
import javafx.collections.FXCollections
import javafx.geometry.Insets
import javafx.scene.control.*
import javafx.scene.layout.AnchorPane
import javafx.scene.layout.BorderPane
import javafx.scene.layout.HBox
import ru.isma.javafx.extensions.controls.cellFactory

private const val LIST_ITEMS_SPACING = 10.0

class NamedPickerItem<T>(val name: String, val value: T)

fun <T> pickItems(item: Iterable<NamedPickerItem<T>>): List<NamedPickerItem<T>> {
    class SelectionItemViewModel(val isSelected: BooleanProperty, val value: NamedPickerItem<T>)

    val selectionItemViewModels = item.map { SelectionItemViewModel(SimpleBooleanProperty(), it) }

    Dialog<ButtonType>().apply {
        title = "Select variables"
        dialogPane.content = BorderPane().apply {
            center = ListView(FXCollections.observableArrayList(selectionItemViewModels)).apply {
                cellFactory { item ->
                    HBox(
                        CheckBox().apply {
                            item.isSelected.bind(selectedProperty())
                        },
                        Label(item.value.name)
                    ).apply {
                        spacing = LIST_ITEMS_SPACING
                    }
                }
            }
            bottom = AnchorPane(
                HBox(
                    Button("Select all").apply {
                        setOnAction {
                            selectionItemViewModels.forEach { it.isSelected.value = true }
                        }
                    },
                    Button("Unselect all").apply {
                        setOnAction {
                            selectionItemViewModels.forEach { it.isSelected.value = false }
                        }
                    },
                ).apply {
                    AnchorPane.setRightAnchor(this, 0.0)
                    spacing = LIST_ITEMS_SPACING
                    padding = Insets(5.0, 0.0, 5.0, 0.0)
                })
        }

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