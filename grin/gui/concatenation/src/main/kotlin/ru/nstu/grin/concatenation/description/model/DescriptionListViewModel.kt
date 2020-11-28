package ru.nstu.grin.concatenation.description.model

import javafx.beans.property.SimpleListProperty
import javafx.collections.FXCollections
import ru.nstu.grin.common.model.Description
import tornadofx.ViewModel
import tornadofx.*

class DescriptionListViewModel : ViewModel() {
    var descriptionsProperty = SimpleListProperty<Description>(FXCollections.observableArrayList())
    var descriptions by descriptionsProperty
}