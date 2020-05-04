package ru.nstu.grin.concatenation.file.options.view

import javafx.scene.Parent
import ru.nstu.grin.concatenation.file.options.controller.FileOptionsController
import ru.nstu.grin.concatenation.file.options.model.CsvDetails
import ru.nstu.grin.concatenation.file.options.model.ExcelDetails
import ru.nstu.grin.concatenation.file.options.model.FileOptionsModel
import ru.nstu.grin.concatenation.file.options.model.FileReaderMode
import ru.nstu.grin.concatenation.function.model.FileType
import tornadofx.*

class FileOptionsView : Fragment() {
    private val model: FileOptionsModel by inject(params = params)
    private val controller: FileOptionsController by inject(params = params)

    init {
        when (controller.getType()) {
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
                        textfield().bind(details.rangeProperty)
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
                        text = when (it) {
                            FileReaderMode.ONE_TO_MANY -> "Один ко многим"
                            FileReaderMode.SEQUENCE -> "Функции подряд"
                        }
                    }
                }
            }
        }
        button("ОК") {
            action {
                controller.openPointsWindow()
                close()
            }
        }
    }

}