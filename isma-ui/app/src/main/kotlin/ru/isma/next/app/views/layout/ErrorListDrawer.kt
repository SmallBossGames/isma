package ru.isma.next.app.views.layout

import javafx.scene.control.TitledPane
import ru.isma.next.app.views.toolbars.IsmaErrorListTable

class ErrorListDrawer(
    ismaErrorListTable: IsmaErrorListTable
) : TitledPane("Error list", ismaErrorListTable).apply {
    isCollapsible = true
    isExpanded = false
}
