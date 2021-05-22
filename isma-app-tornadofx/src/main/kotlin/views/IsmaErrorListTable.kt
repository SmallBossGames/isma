package views

import models.SyntaxErrorModel
import services.SyntaxErrorService
import tornadofx.SmartResize
import tornadofx.View
import tornadofx.readonlyColumn
import tornadofx.tableview


class IsmaErrorListTable : View() {
    private val syntaxErrorService: SyntaxErrorService by di()

    override val root = tableview(syntaxErrorService.errors) {
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