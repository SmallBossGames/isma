package ru.nstu.grin.concatenation.file.options.view

import javafx.scene.Parent
import ru.nstu.grin.concatenation.file.options.controller.FileOptionsController
import ru.nstu.grin.concatenation.file.options.model.CsvDetails
import ru.nstu.grin.concatenation.file.options.model.ExcelDetails
import ru.nstu.grin.concatenation.file.options.model.FileReaderMode
import ru.nstu.grin.concatenation.file.utilities.getFileType
import ru.nstu.grin.concatenation.function.model.FileModel
import ru.nstu.grin.concatenation.function.model.FileType
import tornadofx.*

class FileOptionsView : Fragment() {
    private val model: FileModel by inject()
    private val controller: FileOptionsController by inject()

    init {
        when (getFileType(model.file)) {
            FileType.XLS, FileType.XLSX -> {
                model.details = ExcelDetails()
            }
            FileType.CSV -> {
                model.details = CsvDetails()
            }
            else -> close()
        }
    }

    override val root: Parent = form {
        fieldset("Опции чтения") {
            when (val details = model.details) {
                is ExcelDetails -> {
                    field("Название таблицы") {
                        textfield().bind(details.sheetNameProperty)
                    }
                    field("Диапозон ячеек") {
                        textfield(details.rangeProperty)
                    }
                }
                is CsvDetails -> {
                    field("Разделитель") {
                        textfield().bind(details.delimiterProperty)
                    }
                }
            }
            field("Вид файла") {
                combobox(model.readerModeProperty, FileReaderMode.values().toList()) {
                    cellFormat {
                        text = when (it!!) {
                            FileReaderMode.ONE_TO_MANY -> "Один ко многим"
                            FileReaderMode.SEQUENCE -> "Функции подряд"
                        }
                    }
                }
            }
        }
        button("Прочитать") {
            action {
                when (val details = model.details) {
                    is ExcelDetails -> {
                        if (!details.range.contains(":")) {
                            error("Формат должен быть записан в следующем виде A0:B2")
                        } else {
                            controller.openPointsWindow()
                            close()
                        }
                    }
                    is CsvDetails -> {
                        controller.openPointsWindow()
                        close()
                    }
                }
            }
        }
    }

}