package ru.isma.next.app.viewmodels

import javafx.stage.Stage
import ru.isma.next.app.models.preferences.DefaultFilesPreferencesModel
import ru.isma.next.app.models.preferences.WindowPreferencesModel
import ru.isma.next.app.services.preferences.PreferencesProvider
import ru.isma.next.app.services.project.ProjectFileService

class WindowViewModel(
    private val preferencesProvider: PreferencesProvider,
    private val projectFileService: ProjectFileService,
) {
    fun applyPreferences(stage: Stage) {
        val windowProps = preferencesProvider.preferences.windowPreferences
        val defaultFilesPreferences = preferencesProvider.preferences.defaultFilesPreferencesModel

        stage.isMaximized = windowProps.isMaximized
        stage.height = windowProps.height
        stage.width = windowProps.width
        stage.x = windowProps.x
        stage.y = windowProps.y

        projectFileService.open(*defaultFilesPreferences.lastOpenedProjectPath)
    }

    fun capture(stage: Stage) {
        val windowPreferences = WindowPreferencesModel(
            isMaximized = stage.isMaximized,
            height = stage.height,
            width = stage.width,
            x = stage.x,
            y = stage.y,
        )

        val defaultFilesPreferences = DefaultFilesPreferencesModel(
            lastOpenedProjectPath = projectFileService.listAllFilesPaths().toTypedArray()
        )

        preferencesProvider.commit(windowPreferences)
        preferencesProvider.commit(defaultFilesPreferences)
    }
}
