package ru.isma.next.app.services.project

import ru.nstu.isma.compiler.hsm.core.HSM

sealed interface LismaPdeTranslationResult

class SuccessTranslation(val hsm: HSM) : LismaPdeTranslationResult
data object FailedTranslation : LismaPdeTranslationResult