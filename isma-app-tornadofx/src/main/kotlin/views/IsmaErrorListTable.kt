package views

import controllers.SyntaxErrorController
import javafx.scene.Parent
import models.SyntaxErrorModel
import tornadofx.*

class IsmaErrorListTable : View() {
    private val syntaxErrorController: SyntaxErrorController by inject()

    override val root = tableview(syntaxErrorController.errors) {
        maxHeight = 200.0
        readonlyColumn("Row", SyntaxErrorModel::row).cellFormat {
            text = if (it > 0) it.toString() else ""
        }
        readonlyColumn("Position", SyntaxErrorModel::position).cellFormat {
            text = if (it > 0) it.toString() else ""
        }
        readonlyColumn("Message", SyntaxErrorModel::message)
        columnResizePolicy = SmartResize.POLICY
    }
}