package ru.isma.javafx.extensions.controls

import javafx.beans.property.Property
import javafx.collections.ObservableList
import javafx.scene.control.ComboBox
import javafx.scene.control.ListCell

fun <T> comboBox(
    variants: ObservableList<T>,
    property: Property<T>,
    cellFactory: () -> ListCell<T>
) = ComboBox(variants).apply {
    buttonCell = cellFactory()
    setCellFactory {
        cellFactory()
    }
    valueProperty().bindBidirectional(property)
}