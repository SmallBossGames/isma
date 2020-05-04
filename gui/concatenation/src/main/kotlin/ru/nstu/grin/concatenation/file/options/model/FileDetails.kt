package ru.nstu.grin.concatenation.file.options.model

import javafx.beans.property.SimpleStringProperty

sealed class FileDetails

class ExcelDetails(
    var rangeProperty: SimpleStringProperty = SimpleStringProperty(),
    var sheetNameProperty: SimpleStringProperty = SimpleStringProperty()
) : FileDetails()

class CsvDetails(
    var delimiterProperty: SimpleStringProperty = SimpleStringProperty()
) : FileDetails()