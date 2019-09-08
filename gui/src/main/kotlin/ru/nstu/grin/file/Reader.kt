package ru.nstu.grin.file

import ru.nstu.grin.model.FormType
import java.io.File

/**
 * @author Konstantin Volivach
 */
interface Reader {

    fun read(file: File): FormType
}