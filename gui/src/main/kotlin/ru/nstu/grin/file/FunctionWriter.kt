package ru.nstu.grin.file

import ru.nstu.grin.model.FormType
import java.io.File
import java.io.FileOutputStream

class FunctionWriter(private val file: File) : Writer {
    override fun write(bytes: FormType) {
        FileOutputStream(file).use {
            it.write(bytes.toByteArray())
        }
    }
}