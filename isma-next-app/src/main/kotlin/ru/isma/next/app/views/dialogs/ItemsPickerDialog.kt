package ru.isma.next.app.views.dialogs

import javafx.beans.property.BooleanProperty
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.collections.FXCollections
import javafx.geometry.Insets
import javafx.scene.control.*
import javafx.scene.layout.AnchorPane
import javafx.scene.layout.BorderPane
import javafx.scene.layout.HBox
import javafx.scene.layout.VBox
import ru.isma.javafx.extensions.controls.cellFactory
import ru.isma.javafx.extensions.controls.comboBox

private const val LIST_ITEMS_SPACING = 10.0

data class NamedPickerItem<T>(val name: String, val value: T)
data class NamedPickerModel<T>(
    val xAxisItem: NamedPickerItem<T>,
    val yAxisItems: List<NamedPickerItem<T>>
)

fun <T> pickAxisVariables(
    model: NamedPickerModel<T>
): NamedPickerModel<T>? {
    class SelectionItemViewModel(val isSelected: BooleanProperty, val value: NamedPickerItem<T>)

    val selectionXAxisItem = SimpleObjectProperty(model.xAxisItem)
    val selectionYAxisItems = model.yAxisItems.map { SelectionItemViewModel(SimpleBooleanProperty(), it) }

    Dialog<ButtonType>().apply {
        title = "Select variables"
        dialogPane.content = BorderPane().apply {
            top = VBox(
                Label("X Axis"),
                comboBox(FXCollections.observableArrayList(model.yAxisItems), selectionXAxisItem) {
                    object: ListCell<NamedPickerItem<T>>() {
                        override fun updateItem(item: NamedPickerItem<T>?, empty: Boolean) {
                            super.updateItem(item, empty)

                            text = item?.name
                        }
                    }
                }.apply {
                    maxWidth = Double.MAX_VALUE
                },
                Label("Y Axis"),
                ListView(FXCollections.observableArrayList(selectionYAxisItems)).apply {
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
            )
            bottom = AnchorPane(
                HBox(
                    Button("Select all").apply {
                        setOnAction {
                            selectionYAxisItems.forEach { it.isSelected.value = true }
                        }
                    },
                    Button("Unselect all").apply {
                        setOnAction {
                            selectionYAxisItems.forEach { it.isSelected.value = false }
                        }
                    },
                ).apply {
                    AnchorPane.setRightAnchor(this, 0.0)
                    spacing = LIST_ITEMS_SPACING
                    padding = Insets(5.0, 0.0, 5.0, 0.0)
                }
            )
        }

        dialogPane.buttonTypes.addAll(
            ButtonType("Ok", ButtonBar.ButtonData.OK_DONE),
            ButtonType("Close", ButtonBar.ButtonData.CANCEL_CLOSE),
        )

        val result = showAndWait()

        return if (result.isPresent && result.get().buttonData == ButtonBar.ButtonData.OK_DONE) {
            NamedPickerModel(
                selectionXAxisItem.value,
                selectionYAxisItems.filter { it.isSelected.value }.map { it.value }
            )
        } else {
            null
        }
    }
}