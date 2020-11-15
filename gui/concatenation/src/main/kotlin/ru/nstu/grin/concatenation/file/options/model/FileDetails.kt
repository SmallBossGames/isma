package ru.nstu.grin.concatenation.file.options.model

import javafx.beans.property.SimpleStringProperty
import tornadofx.*

sealed class FileDetails

class ExcelDetails : FileDetails() {
    var rangeProperty: SimpleStringProperty = SimpleStringProperty(this, "rangeProperty", "A0:B1")
    var range by rangeProperty

    var sheetNameProperty: SimpleStringProperty = SimpleStringProperty()
    var sheetName by sheetNameProperty
}

class CsvDetails : FileDetails() {
    var delimiterProperty: SimpleStringProperty = SimpleStringProperty()
    var delimiter by delimiterProperty
}