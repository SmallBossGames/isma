package ru.nstu.isma.grin.launcher

import javafx.application.Application
import javafx.scene.Scene
import javafx.scene.image.Image
import javafx.stage.Stage
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import ru.nstu.grin.concatenation.canvas.view.ConcatenationView
import ru.nstu.grin.concatenation.koin.MainGrinScope

class GrinApplication: Application(), KoinComponent {
    lateinit var scope: MainGrinScope

    init {
        grinKoinStart()
    }

    override fun start(primaryStage: Stage) {
        scope = MainGrinScope(primaryStage)

        val view = scope.get<ConcatenationView>()
        val scene = Scene(view)

        scope.primaryStage.initWindow(scene, "GrIn 22")
        scope.primaryStage.show()
    }

    override fun stop() {
        scope.scope.close()
    }

    private fun Stage.initWindow(scene: Scene, title: String) {
        this.title = title
        this.scene = scene

        icons.add(Image("/ru/nstu/grin/integration/isma-2016-title.png"))

        isMaximized = false
        height = 600.0
        width = 800.0

        minHeight = 500.0
        minWidth = 600.0
    }
}