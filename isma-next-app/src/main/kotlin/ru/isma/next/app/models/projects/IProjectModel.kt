package ru.isma.next.app.models.projects

import javafx.beans.property.SimpleStringProperty
import java.io.File

interface IProjectModel {
    var name: String

    var file: File?

    fun nameProperty(): SimpleStringProperty

    fun snapshot(): LismaTextModel
}