package ru.nstu.isma.hsm.`var`.pde

import ru.nstu.isma.hsm.`var`.HMConst
import java.io.Serializable

/**
 * Bessonov Alex.
 * Date: 04.12.13 Time: 0:17
 */
class HMSampledSpatialVariable : HMSpatialVariable(), Serializable {
    // апроксимировать по шагу или количеству точек
    var type: ApproximateType? = null

    // в зависимости от типа - шаг или количество точек
    var apxVal: HMConst? = null
    val stepSize: Double?
        get() {
            var out: Double? = null
            if (type == ApproximateType.BY_NUMBER_OF_PIECES) {
                out = (valTo!!.value!! - valFrom!!.value!!) / apxVal!!.value!!
            } else if (type == ApproximateType.BY_STEP) {
                out = apxVal!!.value
            }
            return out
        }
    val pointsCount: Int
        get() {
            var out: Double? = null
            if (type == ApproximateType.BY_NUMBER_OF_PIECES) {
                out = apxVal!!.value
            } else if (type == ApproximateType.BY_STEP) {
                out = (valTo!!.value!! - valFrom!!.value!!) / apxVal!!.value!!
            }
            out = Math.ceil(out!!)
            return out.toInt()
        }

    enum class ApproximateType : Serializable {
        BY_STEP, BY_NUMBER_OF_PIECES
    }
}