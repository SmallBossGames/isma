package ru.nstu.grin.concatenation.description.model

import javafx.collections.FXCollections
import ru.nstu.grin.common.model.Description
import tornadofx.ViewModel

class DescriptionListViewModel : ViewModel() {
    val descriptions = FXCollections.observableArrayList<Description>()!!
}