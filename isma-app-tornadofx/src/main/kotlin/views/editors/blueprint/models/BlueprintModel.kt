package views.editors.blueprint.models

class BlueprintModel(
    val main: BlueprintStateModel,
    val init: BlueprintStateModel,
    val states: Array<BlueprintStateModel>,
    val transactions: Array<BlueprintTransactionModel>
)