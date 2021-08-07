package ru.nstu.isma.lisma

import error.IsmaErrorList
import ru.nstu.isma.core.hsm.HSM

/**
 * Created by Bessonov Alex
 * on 11.10.2014.
 */
interface InputTranslator {
    fun translate(text: String, errors: IsmaErrorList): HSM
}