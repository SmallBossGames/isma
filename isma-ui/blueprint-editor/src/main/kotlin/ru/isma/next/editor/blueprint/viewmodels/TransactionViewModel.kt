package ru.isma.next.editor.blueprint.viewmodels

import javafx.beans.property.SimpleStringProperty
import javafx.beans.property.StringProperty
import javafx.beans.binding.Bindings

class TransactionViewModel(
    startStateName: String = "",
    endStateName: String = "",
    predicate: String = "",
    alias: String = ""
) {
    val startStateNameProperty: StringProperty = SimpleStringProperty(startStateName)
    var startStateName: String
        get() = startStateNameProperty.value
        set(value) { startStateNameProperty.value = value }

    val endStateNameProperty: StringProperty = SimpleStringProperty(endStateName)
    var endStateName: String
        get() = endStateNameProperty.value
        set(value) { endStateNameProperty.value = value }

    val predicateProperty: StringProperty = SimpleStringProperty(predicate)
    var predicate: String
        get() = predicateProperty.value
        set(value) { predicateProperty.value = value }

    val aliasProperty: StringProperty = SimpleStringProperty(alias)
    var alias: String
        get() = aliasProperty.value
        set(value) { aliasProperty.value = value }

    val displayText = Bindings.createStringBinding(
        {
            val aliasValue = aliasProperty.value
            if (aliasValue != null && aliasValue.isNotBlank()) aliasValue else predicateProperty.value
        },
        aliasProperty,
        predicateProperty
    )
}
