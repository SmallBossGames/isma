package ru.isma.next.editor.blueprint.models

import javafx.beans.property.SimpleStringProperty
import tornadofx.*

class LismaStateModel {
    private val textProperty = SimpleStringProperty("")
    private val titleProperty = SimpleStringProperty("")

    fun textProperty() = textProperty
    fun titleProperty() = titleProperty

    var text by textProperty
    var title by titleProperty
}