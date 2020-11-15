package ru.nstu.grin.concatenation.cartesian.model

import javafx.beans.property.SimpleListProperty
import tornadofx.ViewModel
import tornadofx.*

class CartesianListViewModel : ViewModel() {
    var cartesianSpacesProperty = SimpleListProperty<CartesianSpace>()
    var cartesianSpaces by cartesianSpacesProperty
}