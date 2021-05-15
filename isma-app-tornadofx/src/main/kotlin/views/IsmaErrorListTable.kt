package views

import services.SyntaxErrorService
import models.SyntaxErrorModel
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject as koinInject
import tornadofx.*


class IsmaErrorListTable : View(), KoinComponent {
    private val syntaxErrorService: SyntaxErrorService by koinInject()

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