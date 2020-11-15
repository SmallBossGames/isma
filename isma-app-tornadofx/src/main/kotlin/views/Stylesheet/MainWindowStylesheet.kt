package views.Stylesheet

import javafx.scene.paint.Color
import tornadofx.Stylesheet
import tornadofx.box
import tornadofx.cssclass


class MainWindowStylesheet : Stylesheet() {
    companion object {
        val toolbarButton by cssclass()
    }

    init {
        toolbarButton {
            borderColor += box(Color.rgb(0, 0, 0, 0.0))
            backgroundColor += Color.rgb(0, 0, 0, 0.0)
        }
    }
}