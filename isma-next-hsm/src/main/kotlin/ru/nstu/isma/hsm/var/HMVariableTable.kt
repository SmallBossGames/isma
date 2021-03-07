package ru.nstu.isma.hsm.`var`

import ru.nstu.isma.hsm.`var`.pde.HMPartialDerivativeEquation
import ru.nstu.isma.hsm.exp.HMExpression
import java.io.Serializable
import java.util.*

/**
 * Created by Bessonov Alex
 * Date: 24.10.13
 * Time: 23:33
 */
class HMVariableTable : Serializable {
    var parent: HMVariableTable? = null
    protected var variables: MutableMap<String?, HMVariable> = HashMap()
    var setters: Map<String?, HMExpression?> = HashMap()
    operator fun get(key: String?): HMVariable? {
        return if (!variables.containsKey(key) && parent != null) {
            parent!![key]
        } else variables[key]
    }

    fun contain(key: String?): Boolean {
        return variables.containsKey(key)
    }

    fun empty(): Boolean {
        return variables.isEmpty()
    }

    fun clear() {
        variables.clear()
    }

    fun add(item: HMVariable): Boolean {
        if (contain(item.code)) {
            return false
        }
        variables[item.code] = item
        return true
    }

    fun remove(key: String?): Boolean {
        if (!contain(key)) {
            return false
        }
        variables.remove(key)
        return true
    }

    fun keySet(): Set<String?> {
        return variables.keys
    }

    fun variables(): List<HMVariable> {
        return variables.values.toList()
    }

    val odes: List<HMDerivativeEquation>
        get() = variables.values.filterIsInstance<HMDerivativeEquation>()
    val algs: List<HMAlgebraicEquation>
        get() = variables.values.filterIsInstance<HMAlgebraicEquation>()
    val pdes: List<HMPartialDerivativeEquation>
        get() = variables.values.filterIsInstance<HMPartialDerivativeEquation>()
}