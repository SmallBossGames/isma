package ru.isma.next.app.views.layout

import javafx.scene.control.TitledPane
import ru.isma.next.app.views.toolbars.IsmaErrorListTable

class ErrorListDrawer(
    ismaErrorListTable: IsmaErrorListTable
) : TitledPane("Error list", ismaErrorListTable) {
    init {
        isCollapsible = true
        isExpanded = false
    }
}
