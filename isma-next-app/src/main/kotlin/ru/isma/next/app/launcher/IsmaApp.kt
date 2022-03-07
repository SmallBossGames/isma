package ru.isma.next.app.launcher

import ru.isma.next.app.views.MainView
import javafx.application.Application
import javafx.scene.Scene
import javafx.scene.image.Image
import javafx.stage.Stage
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import ru.isma.next.app.models.preferences.DefaultFilesPreferencesModel
import ru.isma.next.app.models.preferences.WindowPreferencesModel
import ru.isma.next.app.services.preferences.PreferencesProvider
import ru.isma.next.app.services.project.ProjectFileService

class IsmaApplication : Application(), KoinComponent {
    lateinit var stage: Stage

    private val projectFileService: ProjectFileService by inject()
    private val preferencesProvider: PreferencesProvider by inject()
    private val mainView: MainView by inject()

    init {
        ismaKoinStart()
    }

    override fun start(stage: Stage) {
        this.stage = stage

        val scene = Scene(mainView)

        stage.initWindow(scene, "ISMA 22")
        stage.icons.add(Image("images/isma-2016-title.png"))
        stage.show()

        // Initialize GRIN Legacy
        //FX.registerApplication(this, stage)
    }

    override fun stop() {
        stage.tearDownWindow()
    }

    private fun Stage.initWindow(scene: Scene, title: String) {
        val windowProps = preferencesProvider.preferences.windowPreferences
        val defaultFilesPreferences = preferencesProvider.preferences.defaultFilesPreferencesModel

        this.title = title
        this.scene = scene

        isMaximized = windowProps.isMaximized
        height = windowProps.height
        width = windowProps.width
        x = windowProps.x
        y = windowProps.y

        minHeight = 500.0
        minWidth = 600.0

        projectFileService.open(*defaultFilesPreferences.lastOpenedProjectPath)
    }

    private fun Stage.tearDownWindow() {
        val preferencesModel = WindowPreferencesModel(
            isMaximized = this.isMaximized,
            height = this.height,
            width = this.width,
            x = this.x,
            y = this.y,
        )

        val defaultFilesPreferences = DefaultFilesPreferencesModel(
            lastOpenedProjectPath = projectFileService.listAllFilesPaths().toTypedArray()
        )

        preferencesProvider.commit(preferencesModel)
        preferencesProvider.commit(defaultFilesPreferences)
    }
}