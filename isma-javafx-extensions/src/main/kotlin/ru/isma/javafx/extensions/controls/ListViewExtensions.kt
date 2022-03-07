package ru.isma.javafx.extensions.controls

import javafx.scene.Node
import javafx.scene.control.ListCell
import javafx.scene.control.ListView

fun <T> ListView<T>.cellFactory(factory: (item: T) -> Node) {
    setCellFactory {
        object : ListCell<T>() {
            override fun updateItem(item: T?, empty: Boolean, ) {
                super.updateItem(item, empty)

                graphic = if (item == null) null else factory(item)
            }
        }
    }
}