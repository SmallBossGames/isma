package ru.nstu.isma.hsm.linear

import java.lang.RuntimeException
import java.util.HashMap
import ru.nstu.isma.hsm.HSM
import ru.nstu.isma.hsm.common.Calculateable
import ru.nstu.isma.hsm.common.IndexMapper
import java.io.Serializable
import java.util.ArrayList

/**
 * Created by Bessonov Alex
 * on 13.03.2015.
 */
class HMLinearSystem(private val hms: HSM) : Calculateable, Serializable {
    private var innerVars: MutableMap<String?, HMLinearVar> = HashMap()
    private var innerEquations: MutableList<HMLinearEquation?>? = null
    private var indexMapper: IndexMapper? = null
    private var matrix: LinearSystemMatrix? = null
    private var readyForCalc = false
    private var size: Int? = null

    private fun init() {
        size = innerVars.size
        innerEquations = ArrayList(size!!)
    }

    fun addVar(name: String?) {
        val `var` = HMLinearVar(name)
        addVar(`var`)
    }

    fun addVar(`var`: HMLinearVar) {
        if (`var`.columnIndex == null) `var`.columnIndex = innerVars.size
        innerVars[`var`.code] = `var`
        init()
        hms.variableTable.add(`var`)
    }

    fun getLinearVariable(code: String?): HMLinearVar? {
        return innerVars[code]
    }

    fun getLinearVariable(index: Int): HMLinearVar? {
        for (v in innerVars.values) {
            if (v.columnIndex == index) return v
        }
        return null
    }

    fun createEquation(): HMLinearEquation {
        if (size == null) throw RuntimeException("Empty variable list")
        val equation = HMLinearEquation(size!!)
        innerEquations!!.add(equation)
        return equation
    }

    val isEmpty: Boolean
        get() = size == null || size == 0

    val vars: Map<String?, HMLinearVar>
        get() = innerVars

    val equations: List<HMLinearEquation?>?
        get() = innerEquations

    fun getVarCalulationIndex(v: HMLinearVar): Int {
        return indexMapper!!.indexMap[v.code]!!
    }

    override fun prepareForCalculation(indexMapper: IndexMapper?) {
        this.indexMapper = indexMapper
        val matrixBuilder = LinearSystemMatrixBuilder(indexMapper)
        matrix = matrixBuilder.build("DefaultLinearSystemMatrix", true)
        readyForCalc = true
    }

    override fun calculate(y: DoubleArray): DoubleArray {
        if (!readyForCalc) throw RuntimeException("Linear system is not ready")
        val solver = LinearEquationsSolver(
            matrix!!.getA(y)
        )
        return solver.solve(matrix!!.getB(y))
    }
}