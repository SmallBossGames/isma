package views.editors.blueprint.models

import views.editors.blueprint.controls.StateBox
import views.editors.blueprint.controls.StateTransactionArrow

data class BluePrintTransactionModel(
    val startStateBox: StateBox,
    val endStateBox: StateBox,
    val transactionArrow: StateTransactionArrow)
