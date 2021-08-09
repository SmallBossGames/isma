package ru.isma.next.app.views.toolbars

import ru.isma.next.common.services.lisma.models.ErrorViewModel
import ru.isma.next.app.services.ModelErrorService
import tornadofx.SmartResize
import tornadofx.View
import tornadofx.readonlyColumn
import tornadofx.tableview


class IsmaErrorListTable : View() {
    private val modelErrorService: ModelErrorService by di()

    override val root = tableview(modelErrorService.errors) {
        maxHeight = 200.0
        readonlyColumn("Row", ErrorViewModel::row).cellFormat {
            text = if (it > 0) it.toString() else ""
        }
        readonlyColumn("Position", ErrorViewModel::position).cellFormat {
            text = if (it > 0) it.toString() else ""
        }
        readonlyColumn("Message", ErrorViewModel::message)
        columnResizePolicy = SmartResize.POLICY
    }
}