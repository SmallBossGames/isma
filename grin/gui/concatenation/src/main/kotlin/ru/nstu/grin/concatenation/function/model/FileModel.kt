package ru.nstu.grin.concatenation.function.model

import javafx.beans.property.SimpleObjectProperty
import ru.nstu.grin.concatenation.file.options.model.FileDetails
import ru.nstu.grin.concatenation.file.options.model.FileReaderMode
import ru.isma.javafx.extensions.helpers.getValue
import ru.isma.javafx.extensions.helpers.setValue
import java.io.File

class FileModel {
    lateinit var file: File
    lateinit var details: FileDetails
    val readerModeProperty = SimpleObjectProperty<FileReaderMode>()
    var readerMode: FileReaderMode by readerModeProperty
}
