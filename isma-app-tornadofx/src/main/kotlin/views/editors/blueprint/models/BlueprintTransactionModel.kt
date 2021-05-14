package views.editors.blueprint.models

class BlueprintTransactionModel (
    val startState: BlueprintStateModel,
    val endState: BlueprintStateModel,
    val predicate: String,
)