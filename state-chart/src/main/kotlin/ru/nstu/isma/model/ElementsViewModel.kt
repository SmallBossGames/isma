package ru.nstu.isma.model

import javafx.beans.property.SimpleListProperty
import javafx.collections.FXCollections
import ru.nstu.isma.model.entity.ElementType
import tornadofx.ViewModel

class ElementsViewModel : ViewModel() {
    val elementsProperty = SimpleListProperty<ElementType>(FXCollections.observableArrayList(
        ElementType.values().toList()))
    var selectedElement: ElementType = ElementType.STATE
}