package utilities

import views.editors.blueprint.models.BlueprintModel

fun BlueprintModel.toJson() {

}

fun BlueprintModel.convertToLisma() : String {
    val resultStringBuilder = StringBuilder().appendLine(this.main.text)

    val stateBlockModels = HashMap<String, StateBlockModel>()
    val statesMap = this.states.associateBy { it.name }

    this.transactions.forEach {
        val key = createTransactionKey(it.endStateName, it.predicate)
        val blockModel = stateBlockModels[key]

        if(blockModel == null) {
            val newBlockModel = StateBlockModel(key, statesMap[it.endStateName]!!.text)
            newBlockModel.inputStates.add(it.endStateName)
            stateBlockModels[key] = newBlockModel
        } else {
            blockModel.inputStates.add(it.startStateName)
        }
    }

    stateBlockModels.values.forEach {
        resultStringBuilder.appendLine(it.toString()).appendLine()
    }

    return resultStringBuilder.toString()
}

private class StateBlockModel(val transactionKey: String, val text: String) {
    val inputStates = ArrayList<String>()

    override fun toString(): String {
        val sb = StringBuilder()
            .appendLine("state $transactionKey {")
            .appendLine(text)
            .append("} ")

        inputStates.forEach {
            sb.append("$it, ")
        }

        sb[sb.lastIndex] = ';'

        return sb.toString()
    }
}

private fun createTransactionKey(targetStateName: String, predicate: String) =
    "${targetStateName.trim()} (${predicate.trim()})"