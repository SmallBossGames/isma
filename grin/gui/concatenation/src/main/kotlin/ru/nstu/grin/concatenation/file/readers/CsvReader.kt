package ru.nstu.grin.concatenation.file.readers

import ru.nstu.grin.concatenation.file.options.model.FileReaderMode
import ru.nstu.grin.concatenation.file.utilities.showError
import java.io.File
import java.io.FileInputStream

class CsvReader {
    fun read(file: File, delimiter: String, readerMode: FileReaderMode): List<List<String>> =
        FileInputStream(file).use {
            val lines = String(it.readBytes()).split("\n")
            val result = mutableListOf<List<String>>()
            for (line in lines) {
                val coordinates = line.split(delimiter)
                if (readerMode == FileReaderMode.SEQUENCE && coordinates.size % 2 != 0) {
                    showError("Неверное количество колонок")
                    return@use emptyList()
                }
                result.add(coordinates)
            }
            result
        }
}