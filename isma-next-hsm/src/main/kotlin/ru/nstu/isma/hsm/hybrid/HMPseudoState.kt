package ru.nstu.isma.hsm.hybrid

import ru.nstu.isma.hsm.exp.HMExpression
import java.io.Serializable
import java.util.*

/**
 * Created by Bessonov Alex
 * on 08.02.2015.
 */
class HMPseudoState : HMState(UUID.randomUUID().toString()), Serializable {
    var condition: HMExpression? = null
}