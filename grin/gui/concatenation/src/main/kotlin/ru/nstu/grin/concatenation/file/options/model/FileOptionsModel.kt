package ru.nstu.grin.concatenation.file.options.model

import javafx.beans.property.SimpleObjectProperty
import tornadofx.ViewModel
import tornadofx.*

class FileOptionsModel : ViewModel() {
    val readerModeProperty = SimpleObjectProperty<FileReaderMode>()
    var readerMode by readerModeProperty
}