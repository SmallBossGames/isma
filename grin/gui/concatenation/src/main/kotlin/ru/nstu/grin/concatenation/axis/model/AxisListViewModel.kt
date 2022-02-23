package ru.nstu.grin.concatenation.axis.model

import javafx.collections.FXCollections
import tornadofx.ViewModel

class AxisListViewModel: ViewModel() {
    val axises = FXCollections.observableArrayList<ConcatenationAxis>()
}