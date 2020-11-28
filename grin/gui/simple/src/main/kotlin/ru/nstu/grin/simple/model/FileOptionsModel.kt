package ru.nstu.grin.simple.model

import javafx.beans.property.SimpleStringProperty
import tornadofx.*
import java.io.File

class FileOptionsModel : ViewModel() {
    val delimiterProperty = SimpleStringProperty()
    var delimiter by delimiterProperty

    val file: File by param()
}