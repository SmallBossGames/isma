package ru.nstu.grin.concatenation.function.model

import javafx.beans.property.SimpleObjectProperty
import ru.nstu.grin.concatenation.file.options.model.FileDetails
import ru.nstu.grin.concatenation.file.options.model.FileReaderMode
import tornadofx.ViewModel
import tornadofx.*
import java.io.File

class FileModel : ViewModel() {
    lateinit var file: File
    lateinit var details: FileDetails
    val readerModeProperty = SimpleObjectProperty<FileReaderMode>()
    var readerMode: FileReaderMode by readerModeProperty
}