package ru.nstu.isma.lisma

import ru.nstu.isma.compiler.hsm.core.models.IsmaErrorList
import ru.nstu.isma.compiler.hsm.core.HSM

/**
 * Created by Bessonov Alex
 * on 11.10.2014.
 */
interface InputTranslator {
    fun translate(text: String, errors: IsmaErrorList): HSM
}