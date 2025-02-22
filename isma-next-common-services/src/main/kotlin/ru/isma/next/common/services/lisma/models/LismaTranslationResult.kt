package ru.isma.next.common.services.lisma.models

import ru.nstu.isma.core.hsm.HSM

sealed interface LismaTranslationResult

class SuccessTranslation(val hsm: HSM) : LismaTranslationResult
data object FailedTranslation : LismaTranslationResult