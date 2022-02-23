package ru.nstu.grin.concatenation.cartesian.model

import javafx.collections.FXCollections
import tornadofx.ViewModel

class CartesianListViewModel : ViewModel() {
    val cartesianSpaces = FXCollections.observableArrayList<CartesianSpace>()!!
}