package ru.isma.next.app.models.preferences

import kotlinx.serialization.Serializable

@Serializable
class DefaultFilesPreferencesModel(
    val lastOpenedProjectPath: Array<String> = emptyArray()
)
