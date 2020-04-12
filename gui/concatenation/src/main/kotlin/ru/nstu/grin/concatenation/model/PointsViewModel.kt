package ru.nstu.grin.concatenation.model

import javafx.beans.property.SimpleListProperty
import javafx.collections.FXCollections
import ru.nstu.grin.common.model.Point
import tornadofx.*
import java.io.File

class PointsViewModel : ViewModel() {
    val pointsListProperty: SimpleListProperty<List<String>> = SimpleListProperty(FXCollections.observableArrayList())
    val pointsList by pointsListProperty

    val file: File by param()
}