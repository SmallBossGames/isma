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

    val nameProperty = SimpleStringProperty("")
    var name: String by nameProperty

    val projectTextProperty =  SimpleStringProperty("")
    var projectText: String by projectTextProperty

    var file: File? = null
}
