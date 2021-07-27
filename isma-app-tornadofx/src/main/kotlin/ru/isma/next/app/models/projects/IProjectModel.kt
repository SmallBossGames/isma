package ru.isma.next.app.models.projects

import javafx.beans.property.SimpleStringProperty
import tornadofx.getProperty
import tornadofx.property
import java.io.File

interface IProjectModel {
    var name: String

    val lismaText: String

    var file: File?

    fun nameProperty(): SimpleStringProperty
}