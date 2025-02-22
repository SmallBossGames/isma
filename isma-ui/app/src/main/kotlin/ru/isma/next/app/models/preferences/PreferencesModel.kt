package ru.isma.next.app.models.preferences

import kotlinx.serialization.Serializable

@Serializable
data class PreferencesModel(
    val windowPreferences: WindowPreferencesModel = WindowPreferencesModel(),
    val defaultFilesPreferencesModel: DefaultFilesPreferencesModel = DefaultFilesPreferencesModel(),
)