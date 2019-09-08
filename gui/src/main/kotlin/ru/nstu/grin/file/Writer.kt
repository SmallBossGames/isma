package ru.nstu.grin.file

import ru.nstu.grin.model.FormType

/**
 * @author Konstantin Volivach
 */
interface Writer {

    fun write(bytes: FormType)
}