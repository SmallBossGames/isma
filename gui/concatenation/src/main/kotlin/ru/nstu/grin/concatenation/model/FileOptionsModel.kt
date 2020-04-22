package ru.nstu.grin.concatenation.model

import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import tornadofx.ViewModel
import tornadofx.*
import java.io.File

class FileOptionsModel : ViewModel() {
    val delimiterProperty = SimpleStringProperty()
    var delimiter by delimiterProperty

    val readerModeProperty = SimpleObjectProperty<FileReaderMode>()
    var readerMode by readerModeProperty

    val file: File by param()
}