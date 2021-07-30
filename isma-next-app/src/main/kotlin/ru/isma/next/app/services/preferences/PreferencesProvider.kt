package ru.isma.next.app.services.preferences

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import ru.isma.next.app.models.preferences.PreferencesModel
import ru.isma.next.app.models.preferences.WindowPreferencesModel
import java.io.File

class PreferencesProvider(val settingsFilePath: String) {
    var preferences: PreferencesModel = load()
        private set

    private fun load() : PreferencesModel {
        val file = File(settingsFilePath)

        return if(file.exists()) {
            Json.decodeFromString(file.readText())
        } else {
            PreferencesModel()
        }
    }

    private fun store() {
        File(settingsFilePath).writeText(Json.encodeToString(preferences))
    }

    fun commit(windowPreferences: WindowPreferencesModel){
        preferences = preferences.copy(windowPreferences = windowPreferences)

        store()
    }
}