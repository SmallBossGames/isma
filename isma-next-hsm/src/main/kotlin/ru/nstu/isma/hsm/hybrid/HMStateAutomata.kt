package ru.nstu.isma.hsm.hybrid

import java.util.stream.Collectors
import ru.nstu.isma.hsm.HSM
import ru.nstu.isma.hsm.exp.HMExpression
import java.io.Serializable
import java.util.*

/**
 * Created by Bessonov Alex
 * Date: 24.10.13
 * Time: 23:32
 */
class HMStateAutomata(protected var parent: HSM) : Serializable {
    protected var innerStates: MutableMap<String?, HMState> = LinkedHashMap()
    var transactions: HashSet<HMTransaction> = LinkedHashSet()
        protected set
    protected var pseudoStates: HashSet<HMPseudoState> = LinkedHashSet()

    var init: HMState? = null

    val states: Map<String?, HMState>
        get() = innerStates

    fun addTransaction(from: HMState?, to: HMState?, condition: HMExpression?) {
        val tr = HMTransaction()
        tr.source = from
        tr.target = to
        tr.condition = condition
        transactions.add(tr)
    }

    fun addState(state: HMState): Boolean {
        if (innerStates.containsKey(state.code)) {
            return false
        }
        innerStates[state.code] = state
        state.variables.parent = parent.variableTable
        return true
    }

    fun getState(key: String?): HMState? {
        return innerStates[key]
    }

    fun getAllFrom(st: HMState): List<HMTransaction> {
        val tr: MutableList<HMTransaction> = LinkedList()
        for (transaction in transactions) {
            if (transaction.source?.code == st.code) {
                tr.add(transaction)
            }
        }
        return tr
    }

    fun getAllTo(st: HMState): List<HMTransaction> {
        val tr: MutableList<HMTransaction> = LinkedList()
        for (transaction in transactions) {
            if (transaction.target?.code == st.code) {
                tr.add(transaction)
            }
        }
        return tr
    }

    fun addPseudoState(ps: HMPseudoState) {
        pseudoStates.add(ps)
        ps.variables.parent = parent.variableTable
    }

    val allPseudoStates: List<HMPseudoState?>
        get() = pseudoStates.stream().collect(Collectors.toList())
}