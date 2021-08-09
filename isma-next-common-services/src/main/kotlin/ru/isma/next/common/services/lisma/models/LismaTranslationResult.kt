package ru.isma.next.common.services.lisma

import ru.nstu.isma.core.hsm.HSM
import ru.nstu.isma.core.hsm.models.IsmaError

sealed interface LismaTranslationResult

class SuccessTranslation(val hsm: HSM) : LismaTranslationResult
class FailedTranslation(val errors: List<IsmaError>) : LismaTranslationResult