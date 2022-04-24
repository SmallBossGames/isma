package ru.nstu.grin.concatenation.canvas.model

class EditModeViewModel {
    var currentEditMode: EditMode = EditMode.NONE

    var moveSettings: IMoveSettings? = null
    var traceSettings: TraceSettings? = null
    val selectionSettings = SelectionSettings()
}