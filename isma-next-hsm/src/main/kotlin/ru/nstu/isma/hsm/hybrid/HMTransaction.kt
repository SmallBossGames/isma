package ru.nstu.isma.hsm.hybrid

import ru.nstu.isma.hsm.exp.HMExpression
import java.io.Serializable

/**
 * Created by Bessonov Alex
 * Date: 24.10.13
 * Time: 23:46
 */
class HMTransaction : Serializable {
    var source: HMState? = null
    var target: HMState? = null
    var condition: HMExpression? = null
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        val that = other as HMTransaction
        return if (source != that.source) false else target == that.target
    }

    override fun hashCode(): Int {
        var result = source.hashCode()
        result = 31 * result + target.hashCode()
        return result
    }
}