package views

import controllers.SyntaxErrorController
import javafx.scene.Parent
import models.SyntaxErrorModel
import tornadofx.View
import tornadofx.readonlyColumn
import tornadofx.tableview

class IsmaErrorListTable : View() {
    private val syntaxErrorController: SyntaxErrorController by inject()

    override val root = tableview(syntaxErrorController.errors) {
        maxHeight = 200.0
        readonlyColumn("Position", SyntaxErrorModel::interval)
        readonlyColumn("Message", SyntaxErrorModel::message)
    }
}