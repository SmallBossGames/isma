package ru.nstu.grin.integration

import javafx.scene.Scene
import javafx.stage.Modality
import javafx.stage.Stage
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.parameter.parametersOf
import ru.nstu.grin.concatenation.canvas.model.InitCanvasData
import ru.nstu.grin.concatenation.canvas.view.ConcatenationView
import ru.nstu.grin.concatenation.cartesian.model.CartesianSpace


class GrinIntegrationFacade: KoinComponent {
    fun open(spaces: List<CartesianSpace>) {
        val initData = InitCanvasData(
            cartesianSpaces = spaces,
            arrows = listOf(),
            descriptions = listOf()
        )

        val view = get<ConcatenationView>{ parametersOf(initData) }

        Stage().apply {
            scene = Scene(view.root)
            title = "GRIN"
            initModality(Modality.WINDOW_MODAL)
            show()
        }
    }
}