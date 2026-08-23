package ru.isma.next.app.services.blueprint

import ru.isma.next.app.models.CodeRegion
import ru.isma.next.app.models.LismaTextModel
import ru.isma.next.editor.blueprint.models.BlueprintLoopTransactionModel
import ru.isma.next.editor.blueprint.models.BlueprintModel
import ru.isma.next.editor.blueprint.models.BlueprintStateModel

fun BlueprintModel.toLismaText(): LismaTextModel {
    val regions = ArrayList<CodeRegion>()
    val sb = StringBuilder()

    val stateBlockModels = LinkedHashMap<String, StateBlockModel>()
    val statesMap = this.states.associateBy { it.name }

    sb.appendLine(main.text)

    this.transactions.forEach {
        val key = createTransactionKey(it.endStateName, it.predicate.ifBlank { LISMA_TRUE })
        val blockModel = stateBlockModels[key]

        if (blockModel == null) {
            StateBlockModel(it.endStateName, key, statesMap[it.endStateName]?.text ?: "").apply {
                inputStates.add(it.startStateName)
                stateBlockModels[key] = this
            }
        } else {
            blockModel.inputStates.add(it.startStateName)
        }
    }

    stateBlockModels.values.forEach { block ->
        appendFragment(sb, regions, block.stateName, block.toString(), extraBlankLines = 1)
    }

    this.loopTransactions.forEach { loop ->
        appendFragment(sb, regions, loop.stateName, loop.toLisma(statesMap), extraBlankLines = 2)
    }

    return LismaTextModel(sb.toString(), regions)
}

private fun appendFragment(
    sb: StringBuilder,
    regions: ArrayList<CodeRegion>,
    name: String,
    fragmentText: String,
    extraBlankLines: Int,
) {
    val startLine = sb.lineCount() + 1
    sb.appendLine(fragmentText)
    repeat(extraBlankLines) { sb.appendLine() }
    val fragmentLines = fragmentText.lines().count() - if (fragmentText.endsWith("\n")) 1 else 0
    regions.add(CodeRegion(name, startLine, startLine + fragmentLines - 1))
}

private fun StringBuilder.lineCount(): Int = count { it == '\n' }

private const val LISMA_TRUE = "1 > 0"

private class StateBlockModel(val stateName: String, val transactionKey: String, val text: String) {
    val inputStates = ArrayList<String>()

    override fun toString(): String {
        val sb = StringBuilder()
            .appendLine("state $transactionKey {")
            .appendLine(text)
            .append("} from ")

        if (inputStates.isEmpty()) {
            val fromIndex = sb.lastIndexOf("from ")
            if (fromIndex >= 0) {
                sb.delete(fromIndex, sb.length)
            }
            sb.append(';')
        } else {
            inputStates.forEach { sb.append("$it,") }
            sb[sb.lastIndex] = ';'
        }

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
        appendLine(states[stateName]?.text ?: "")
        appendLine("} from ${pseudoStateName};")

        appendLine()
    }.toString()
}

private fun createTransactionKey(targetStateName: String, predicate: String) =
    "${targetStateName.trim()} (${predicate.trim()})"
