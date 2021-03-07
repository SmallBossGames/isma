package ru.nstu.isma.hsm.`var`.pde

import ru.nstu.isma.hsm.`var`.HMConst
import ru.nstu.isma.hsm.`var`.HMDerivativeEquation
import java.io.Serializable
import java.util.*

/**
 * Bessonov Alex.
 * Date: 04.12.13 Time: 0:25
 */
class HMPartialDerivativeEquation : HMDerivativeEquation, Serializable {
    private val innerBoundaries: MutableList<HMBoundaryCondition> = LinkedList()
    private val innerVariables: MutableMap<HMSpatialVariable?, Int> = HashMap()
    private val innerParams: MutableMap<HMSpatialVariable, HMConst> = HashMap()

    constructor(code: String?) : super(code) {}
    constructor(eq: HMDerivativeEquation) : super(eq.code) {
        initial = eq.initial
        rightPart = eq.rightPart
    }

    fun addBound(bb: HMBoundaryCondition) {
        innerBoundaries.add(bb)
    }

    fun addSpatialVar(vv: HMSpatialVariable?, order: Int) {
        innerVariables[vv] = order
    }

    val boundaries: List<HMBoundaryCondition>
        get() = innerBoundaries

    val variables: List<HMSpatialVariable?>
        get() = innerVariables.keys.toList()

    fun getVariableOrder(`var`: HMSpatialVariable?): Int? {
        return innerVariables[`var`]
    }

    fun isContains(v: HMSpatialVariable): Boolean {
        for (vvv in variables) {
            if (vvv?.code == v.code) {
                return true
            }
        }
        return false
    }

    val params: Map<HMSpatialVariable, HMConst>
        get() = innerParams

    fun addParam(sv: HMSpatialVariable, c: HMConst) {
        innerParams[sv] = c
    }

    fun getParam(sv: HMSpatialVariable) {
        innerParams[sv]
    }

    fun getBound(sideType: HMBoundaryCondition.SideType, apx: HMSpatialVariable): HMBoundaryCondition {
        val left: MutableList<HMBoundaryCondition> = LinkedList()
        for (b in innerBoundaries) {
            if (b.side == sideType || b.side == HMBoundaryCondition.SideType.BOTH) {
                if (b.spatialVar.code == apx.code) {
                    left.add(b)
                }
            }
        }
        if (left.size != 1) {
            throw RuntimeException("Incorrect count of boundaries for " + sideType.name + " side(s) of " + code + ": " + left.size)
        }
        return left[0]
    }
}