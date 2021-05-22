package models

import ru.nstu.isma.core.hsm.HSM

sealed interface LismaTranslationResult

class SuccessTranslation(val hsm: HSM) : LismaTranslationResult
class FailedTranslation(val errors: List<SyntaxErrorModel>) : LismaTranslationResult