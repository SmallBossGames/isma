package ru.isma.next.app.services.preferences

import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import ru.isma.next.app.models.preferences.DefaultFilesPreferencesModel
import ru.isma.next.app.models.preferences.PreferencesModel
import ru.isma.next.app.models.preferences.WindowPreferencesModel
import java.io.File

class PreferencesProvider(private val settingsFilePath: String) {
    var preferences: PreferencesModel = load()
        private set

    private fun load() : PreferencesModel {
        val file = File(settingsFilePath)

        return if(file.exists()) {
            try {
                Json.decodeFromString(file.readText())
            } catch (e: Exception) {
                PreferencesModel()
            }
        } else {
            PreferencesModel()
        }
    }

    private fun store() {
        try {
            File(settingsFilePath).writeText(Json.encodeToString(preferences))
        } catch (e: java.io.IOException) {
            e.printStackTrace()
        }
    }

    fun commit(windowPreferences: WindowPreferencesModel){
        preferences = preferences.copy(windowPreferences = windowPreferences)

        store()
    }

    fun commit(defaultFilesPreferences: DefaultFilesPreferencesModel){
        preferences = preferences.copy(defaultFilesPreferencesModel = defaultFilesPreferences)

        store()
    }
}