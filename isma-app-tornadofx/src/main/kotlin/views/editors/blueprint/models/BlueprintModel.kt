package views.editors.blueprint.models

import kotlinx.serialization.Serializable
import views.editors.blueprint.constants.INIT_STATE
import views.editors.blueprint.constants.MAIN_STATE

@Serializable
class BlueprintModel(
    val main: BlueprintStateModel,
    val init: BlueprintStateModel,
    val states: Array<BlueprintStateModel>,
    val transactions: Array<BlueprintTransactionModel>
) {
    companion object {
        @JvmStatic
        val empty = BlueprintModel(
            BlueprintStateModel(10.0, 10.0, MAIN_STATE, ""),
            BlueprintStateModel(10.0, 100.0, INIT_STATE, ""),
            emptyArray(),
            emptyArray()
        )
    }
}