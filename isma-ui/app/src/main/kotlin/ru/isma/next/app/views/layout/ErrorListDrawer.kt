package ru.isma.next.app.views.layout

import javafx.scene.control.TitledPane

class ErrorListDrawer(
    ismaErrorListTable: IsmaErrorListTable
) : TitledPane("Error list", ismaErrorListTable) {
    init {
        isCollapsible = true
        isExpanded = false
    }
}
