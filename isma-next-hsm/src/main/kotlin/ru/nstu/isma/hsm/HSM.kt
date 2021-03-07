package ru.nstu.isma.hsm

import ru.nstu.isma.hsm.`var`.HMDerivativeEquation
import ru.nstu.isma.hsm.`var`.HMVariable
import ru.nstu.isma.hsm.`var`.HMVariableTable
import ru.nstu.isma.hsm.exp.HMExpression
import ru.nstu.isma.hsm.hybrid.HMState
import ru.nstu.isma.hsm.hybrid.HMStateAutomata
import ru.nstu.isma.hsm.linear.HMLinearSystem
import java.io.Serializable
import java.util.*

/**
 * Created by Bessonov Alex
 * Date: 25.10.13
 * Time: 0:04
 */
class HSM : Serializable {
    var variableTable: HMVariableTable
        protected set

    var automata: HMStateAutomata

    var linearSystem: HMLinearSystem
        protected set

    var startTime = 0.0
    var endTime = 1.0
    var stepTime = 0.1

    var out: List<String> = LinkedList()

    fun getVariables(): List<HMVariable?>? {
        return variableTable.variables()
    }

    fun setVariables(variables: HMVariableTable) {
        variableTable = variables
    }

    val isPDE: Boolean
        get() = variableTable.pdes.isNotEmpty()

    fun initTimeEquation(start: Double): HSM {
        val TIME = "TIME"
        val baseTable = variableTable
        if (!baseTable.contain(TIME)) {
            val time = HMDerivativeEquation(TIME)
            time.rightPart = HMExpression.Companion.getConst(1.0)
            time.setInitial(start)
            baseTable.add(time)
        } else {
            (baseTable[TIME] as HMDerivativeEquation).setInitial(start)
        }
        return this
    }

    companion object {
        const val INIT_STATE = "init"
    }

    init {
        variableTable = HMVariableTable()
        automata = HMStateAutomata(this)
        linearSystem = HMLinearSystem(this)
        val init = HMState(INIT_STATE)
        automata.init = init
        automata.addState(init)
    }
}