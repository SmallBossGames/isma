package ru.isma.next.app.launcher

import javafx.application.Application
import javafx.scene.Scene
import javafx.scene.image.Image
import javafx.stage.Stage
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.context.stopKoin
import ru.isma.next.app.viewmodels.WindowViewModel
import ru.isma.next.app.views.MainView
import ru.isma.next.external.SimulationServerFacade

class IsmaApplication : Application(), KoinComponent {
    lateinit var stage: Stage

    private val windowViewModel: WindowViewModel by inject()
    private val mainView: MainView by inject()
    private val serverFacade: SimulationServerFacade by inject()

    init {
        ismaKoinStart()
        serverFacade.warmup()
    }

    override fun start(stage: Stage) {
        this.stage = stage

        val scene = Scene(mainView)

        stage.title = "ISMA 22"
        stage.scene = scene
        stage.minHeight = 500.0
        stage.minWidth = 600.0

        windowViewModel.applyPreferences(stage)

        stage.icons.add(Image("/ru/isma/next/app/launcher/isma-2016-title.png"))
        stage.show()

        // Initialize GRIN Legacy
        // FX.registerApplication(this, stage)
    }

    override fun stop() {
        windowViewModel.capture(stage)
        serverFacade.shutdown()
        stopKoin()
    }
}
