package ru.nstu.grin.concatenation.file.options.model

import javafx.beans.property.SimpleStringProperty
import ru.isma.javafx.extensions.helpers.getValue
import ru.isma.javafx.extensions.helpers.setValue

sealed class FileDetails

class ExcelDetails : FileDetails() {
    var rangeProperty: SimpleStringProperty = SimpleStringProperty("A0:B1")
    var range: String by rangeProperty

    var sheetNameProperty: SimpleStringProperty = SimpleStringProperty("")
    var sheetName: String by sheetNameProperty
}

class CsvDetails : FileDetails() {
    var delimiterProperty: SimpleStringProperty = SimpleStringProperty("")
    var delimiter: String by delimiterProperty
}