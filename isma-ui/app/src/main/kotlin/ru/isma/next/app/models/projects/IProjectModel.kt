package ru.isma.next.app.models.projects

import javafx.beans.property.SimpleStringProperty
import javafx.scene.Node
import java.io.File

interface IProjectModel {
    var name: String

    var file: File?

    val editor: Node

    fun nameProperty(): SimpleStringProperty

    fun snapshot(): LismaTextModel

    fun dispose()
}