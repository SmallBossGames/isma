package ru.nstu.isma.next.core.sim.controller.models

import ru.nstu.isma.core.hsm.HSM
import ru.nstu.isma.intg.api.calcmodel.cauchy.CauchyInitials
import ru.nstu.isma.intg.api.methods.IntgMethod
import ru.nstu.isma.next.core.sim.controller.SimulationResultStorageType
import ru.nstu.isma.next.core.sim.controller.parameters.EventDetectionParameters
import ru.nstu.isma.next.core.sim.controller.parameters.ParallelParameters

data class IntegratorApiParameters(
    val hsm: HSM,
    val initials: CauchyInitials,
    val method: IntgMethod,
    val parallelParameters: ParallelParameters?,
    val resultStorageType: SimulationResultStorageType,
    val eventDetectionParameters: EventDetectionParameters?,
    val stepChangeHandlers: List<suspend (Double) -> Unit>
)