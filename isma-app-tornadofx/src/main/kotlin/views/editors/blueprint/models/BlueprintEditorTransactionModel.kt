package views.editors.blueprint.models

import views.editors.blueprint.controls.StateBox
import views.editors.blueprint.controls.StateTransactionArrow

data class BlueprintEditorTransactionModel(
    val startStateBox: StateBox,
    val endStateBox: StateBox,
    val transactionArrow: StateTransactionArrow)
