package ru.nstu.isma.next.core.fdm

import ru.nstu.isma.core.hsm.`var`.HMConst
import ru.nstu.isma.core.hsm.`var`.pde.HMSampledSpatialVariable

/**
 * Created by Bessonov Alex
 * Date: 12.12.13
 * Time: 1:02
 * Класс индекса апроксимируемой переменной
 */
class FDMIndexedApxVar(v: HMSampledSpatialVariable) : HMSampledSpatialVariable() {
    var index: Int? = null
        protected set

    // связываем текущий объект с объектом HMApproximateVariable
    // все изменения в базовых полях индекса дожны отразиьться в предке
    fun linkWithHMApproximateVariable(v: HMSampledSpatialVariable) {
        valFrom = v.valFrom
        valTo = v.valTo
        type = v.type
        apxVal = v.apxVal
        code = v.code
    }

    fun setIndex(index: Int) {
        if (!validate(index)) {
            throw RuntimeException("index is not valid! index ="
                    + index.toString() + " but range = [1; " + pointsCount.toString() + "].")
        }
        this.index = index
    }

    // индекс может быть в диапазоне [1; PointsCount]
    val isValid: Boolean
        get() = validate(index!!)

    private fun validate(idx: Int): Boolean {
        return idx > 0 && idx <= pointsCount
    }

    val isMax: Boolean
        get() = index == pointsCount
    val isFirst: Boolean
        get() = index == 1

    fun inc() {
        index = index!! + 1
    }

    val value: Double
        get() = getValFrom().value + (index!! - 1) * stepSize

    fun toConst(): HMConst {
        val hmConst = HMConst(constCode)
        hmConst.value = value
        return hmConst
    }

    val constCode: String
        get() = code + APX_PREFIX + "_" + index

    init {
        linkWithHMApproximateVariable(v)
    }
}