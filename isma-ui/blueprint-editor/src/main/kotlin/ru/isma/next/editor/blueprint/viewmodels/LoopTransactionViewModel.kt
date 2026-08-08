package ru.isma.next.editor.blueprint.viewmodels

import javafx.beans.binding.Bindings
import javafx.beans.binding.ObjectBinding
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleStringProperty
import javafx.beans.property.StringProperty

class LoopTransactionViewModel(
    stateName: String = "",
    predicate: String = "",
    alias: String = "",
    text: String = "",
    selected: Boolean = false
) {
    val stateNameProperty = SimpleStringProperty(stateName)
    val predicateProperty = SimpleStringProperty(predicate)
    val aliasProperty = SimpleStringProperty(alias)
    val textProperty = SimpleStringProperty(text)
    val selectedProperty = SimpleBooleanProperty(selected)

    var stateName: String
        get() = stateNameProperty.value
        set(value) {
            stateNameProperty.value = value
        }

    var predicate: String
        get() = predicateProperty.value
        set(value) {
            predicateProperty.value = value
        }

    var alias: String
        get() = aliasProperty.value
        set(value) {
            aliasProperty.value = value
        }

    var text: String
        get() = textProperty.value
        set(value) {
            textProperty.value = value
        }

    var selected: Boolean
        get() = selectedProperty.value
        set(value) {
            selectedProperty.value = value
        }

    val displayText: ObjectBinding<String> = Bindings.createObjectBinding(
        {
            alias.ifBlank { predicate }
        },
        aliasProperty,
        predicateProperty
    )
}
