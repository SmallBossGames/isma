package ru.nstu.isma.grin.launcher

import javafx.application.Application
import javafx.scene.Scene
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
        scope.closeScope()
    }

    private fun Stage.initWindow(scene: Scene, title: String) {
        this.title = title
        this.scene = scene

        isMaximized = false
        height = 600.0
        width = 800.0

        minHeight = 500.0
        minWidth = 600.0
    }
}