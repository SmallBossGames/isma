import views.IsmaMenuBar
import views.IsmaToolBar
import controllers.SyntaxErrorController
import javafx.scene.control.ToggleGroup
import models.SyntaxErrorModel
import tornadofx.*
import views.IsmaEditorTabPane
import views.IsmaErrorListTable

class MyView : View() {

    private val syntaxErrorController: SyntaxErrorController by inject()
    private val ismaMenuBar: IsmaMenuBar by inject()
    private val ismaToolBar: IsmaToolBar by inject()
    private val ismaErrorListTable: IsmaErrorListTable by inject()
    private val ismaEditorTabPane: IsmaEditorTabPane by inject()

    private val toggleGroup = ToggleGroup()

    init {
        title = "ISMA Next"
    }

    override val root = borderpane {
        minHeight = 480.0
        minWidth = 640.0

        top = vbox {
            add(ismaMenuBar)
            add(ismaToolBar)
        }

        center {
            add(ismaEditorTabPane)
        }

        bottom {
            add(ismaErrorListTable)
        }

        syntaxErrorController.errors.add(SyntaxErrorModel(1, 2, "Dick"))
    }

    /*textarea {
            prefHeightProperty().bind(this@vbox.heightProperty())
        }*/
    /*textarea {
            prefHeightProperty().bind(this@vbox.heightProperty())
        }*/
}