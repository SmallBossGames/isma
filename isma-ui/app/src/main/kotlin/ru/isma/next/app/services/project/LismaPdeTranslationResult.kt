package ru.isma.next.app.services.project

sealed interface LismaPdeTranslationResult
data object SuccessTranslation : LismaPdeTranslationResult
data object FailedTranslation : LismaPdeTranslationResult