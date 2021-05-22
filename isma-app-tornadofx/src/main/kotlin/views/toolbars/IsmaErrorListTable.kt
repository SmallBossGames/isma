package views.toolbars

import models.SyntaxErrorModel
import services.ModelErrorService
import tornadofx.SmartResize
import tornadofx.View
import tornadofx.readonlyColumn
import tornadofx.tableview


class IsmaErrorListTable(
    modelErrorService: ModelErrorService
) : View() {
    override val root = tableview(modelErrorService.errors) {
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