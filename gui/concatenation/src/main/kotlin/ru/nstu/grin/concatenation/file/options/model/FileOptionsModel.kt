package ru.nstu.grin.concatenation.file.options.model

import javafx.beans.property.SimpleObjectProperty
import javafx.beans.property.SimpleStringProperty
import tornadofx.ViewModel
import tornadofx.*
import java.io.File

class FileOptionsModel : ViewModel() {
    val readerModeProperty = SimpleObjectProperty<FileReaderMode>()
    var readerMode by readerModeProperty
}