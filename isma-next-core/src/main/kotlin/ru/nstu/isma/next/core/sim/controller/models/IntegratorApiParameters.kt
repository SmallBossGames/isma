package ru.nstu.isma.next.core.sim.controller.models

import ru.nstu.isma.core.hsm.HSM
import ru.nstu.isma.intg.api.calcmodel.cauchy.CauchyInitials

data class IntegratorApiParameters(
    val hsm: HSM,
    val initials: CauchyInitials,
    val stepChangeHandlers: (Double) -> Unit
)
