package models

import javafx.beans.property.SimpleStringProperty
import tornadofx.*
import tornadofx.property
import java.io.File

class IsmaProjectModel {

    constructor(name: String){
        this.name = name
    }

    constructor(file: File){
        this.file = file
        this.name = file.name
        this.projectText = file.readText()
    }

    fun nameProperty() = getProperty(IsmaProjectModel::name)
    var name: String by property<String>()

    fun projectTextProperty() = getProperty(IsmaProjectModel::projectText)
    var projectText: String by property("")

    var file: File? = null
}
