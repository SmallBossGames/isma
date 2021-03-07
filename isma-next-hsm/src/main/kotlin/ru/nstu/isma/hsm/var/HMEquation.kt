package ru.nstu.isma.hsm.`var`

import ru.nstu.isma.hsm.exp.HMExpression
import java.io.Serializable

/**
 * Created by Bessonov Alex
 * Date: 24.10.13
 * Time: 23:29
 */
open class HMEquation : HMVariable(), Serializable {
    open var rightPart: HMExpression? = null
}