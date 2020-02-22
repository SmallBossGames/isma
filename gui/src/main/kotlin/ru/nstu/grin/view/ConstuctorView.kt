package ru.nstu.grin.view

import javafx.scene.Parent
import javafx.scene.layout.Priority
import javafx.stage.StageStyle
import ru.nstu.grin.view.concatenation.ConcatenationView
import ru.nstu.grin.view.kube.KubeCanvasView
import ru.nstu.grin.view.simple.SimpleCanvasView
import tornadofx.*

/**
 * @author kostya05983
 * MainView contains all components for grin graphic builder
 */
class ConstuctorView : View() {

    override val root: Parent = hbox {
        imageview("concatenation_chart.png") {
            vgrow = Priority.ALWAYS
            fitHeight = 300.0
            fitWidth = 300.0
            shortpress {
                find<ConcatenationView>().openWindow(
                    stageStyle = StageStyle.DECORATED
                )
            }
        }
        imageview("usual_chart.png") {
            vgrow = Priority.ALWAYS
            fitHeight = 300.0
            fitWidth = 300.0
            shortpress {
                find<SimpleCanvasView>().openWindow(
                    stageStyle = StageStyle.DECORATED
                )
            }
        }
        imageview("3d_chart.png") {
            vgrow = Priority.ALWAYS
            fitHeight = 300.0
            fitWidth = 300.0
            shortpress {
                find<KubeCanvasView>().openWindow(
                    stageStyle = StageStyle.DECORATED
                )
            }
        }
    }
}