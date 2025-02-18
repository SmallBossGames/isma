package ru.isma.next.app.utilities

import ru.isma.next.app.models.projects.CodeRegion
import ru.isma.next.app.models.projects.LismaTextModel
import ru.isma.next.editor.blueprint.models.BlueprintLoopTransactionModel
import ru.isma.next.editor.blueprint.models.BlueprintModel
import ru.isma.next.editor.blueprint.models.BlueprintStateModel

private const val LISMA_TRUE = "1 > 0"

fun BlueprintModel.convertToLisma() : LismaTextModel {
    val fragments = LinkedHashSet<CodeRegion>()
    val resultStringBuilder = StringBuilder()

    val stateBlockModels = HashMap<String, StateBlockModel>()
    val statesMap = this.states.associateBy { it.name }

    val mainTextFragment = this.main.text

    var linesCounter = mainTextFragment.lines().count() + 1

    resultStringBuilder.appendLine(mainTextFragment)

    this.transactions.forEach {
        val key = createTransactionKey(it.endStateName, it.predicate.ifBlank { LISMA_TRUE })
        val blockModel = stateBlockModels[key]

        if(blockModel == null) {
            StateBlockModel(it.endStateName, key, statesMap[it.endStateName]!!.text).apply {
                inputStates.add(it.startStateName)
                stateBlockModels[key] = this
            }
        } else {
            blockModel.inputStates.add(it.startStateName)
        }
    }

    stateBlockModels.values.forEach {
        val fragmentText = it.toString()
        val fragmentLinesCount = fragmentText.lines().count()
        val startLineNumber = linesCounter
        val endLineNumber = linesCounter + fragmentLinesCount + 1

        resultStringBuilder.appendLine(fragmentText).appendLine()

        fragments.add(CodeRegion(it.stateName, startLineNumber, endLineNumber))

        linesCounter = endLineNumber
    }

    this.loopTransactions.forEach {
        val fragmentText = it.toLisma(statesMap)
        val fragmentLinesCount = fragmentText.lines().count()
        val startLineNumber = linesCounter
        val endLineNumber = linesCounter + fragmentLinesCount + 1

        resultStringBuilder.appendLine(fragmentText).appendLine()

        fragments.add(CodeRegion(it.stateName, startLineNumber, endLineNumber))

        linesCounter = endLineNumber

        resultStringBuilder.appendLine()
    }

    return LismaTextModel(resultStringBuilder.toString(), fragments.toList())
}

private class StateBlockModel(val stateName: String, val transactionKey: String, val text: String) {
    val inputStates = ArrayList<String>()

    override fun toString(): String {
        val sb = StringBuilder()
            .appendLine("state $transactionKey {")
            .appendLine(text)
            .append("} from ")

        inputStates.forEach {
            sb.append("$it,")
        }

        sb[sb.lastIndex] = ';'

        return sb.toString()
    }
}

private fun BlueprintLoopTransactionModel.toLisma(states: Map<String, BlueprintStateModel>): String {
    return StringBuilder().apply {
        val pseudoStateName = "${stateName}_pseudo_1"

        appendLine("state $pseudoStateName (${predicate.trim()}) {")
        appendLine(text)
        appendLine("} from ${stateName};")

        appendLine()

        appendLine("state $stateName ($LISMA_TRUE) {")
        appendLine(states[stateName]!!.text)
        appendLine("} from ${pseudoStateName};")

        appendLine()
    }.toString()
}

private fun createTransactionKey(targetStateName: String, predicate: String) =
    "${targetStateName.trim()} (${predicate.trim()})"