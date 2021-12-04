package ru.nstu.isma.next.core.sim.controller.models

import ru.nstu.isma.intg.api.calcmodel.HybridSystem
import ru.nstu.isma.next.core.simulation.gen.EquationIndexProvider

data class HsmCompilationResult(
    val indexProvider: EquationIndexProvider,
    val hybridSystem: HybridSystem,
    val classLoader: ClassLoader,
)