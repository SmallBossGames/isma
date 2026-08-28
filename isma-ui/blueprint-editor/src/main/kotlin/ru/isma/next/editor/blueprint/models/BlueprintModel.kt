package ru.isma.next.editor.blueprint.models

import ru.isma.next.editor.blueprint.constants.INIT_STATE
import ru.isma.next.editor.blueprint.constants.MAIN_STATE

class BlueprintModel(
    val main: BlueprintStateModel,
    val init: BlueprintStateModel,
    val states: Array<BlueprintStateModel>,
    val transactions: Array<BlueprintTransactionModel>,
    val loopTransactions: Array<BlueprintLoopTransactionModel> = emptyArray()
) {
    companion object {
        @JvmStatic
        val empty = BlueprintModel(
            BlueprintStateModel(10.0, 10.0, MAIN_STATE, ""),
            BlueprintStateModel(10.0, 100.0, INIT_STATE, ""),
            emptyArray(),
            emptyArray(),
            emptyArray(),
        )
    }
}