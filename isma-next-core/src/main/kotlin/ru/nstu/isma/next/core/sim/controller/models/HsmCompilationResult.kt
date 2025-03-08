package ru.nstu.isma.next.core.sim.controller.models

import ru.nstu.isma.compiler.hsm.jvm.EquationIndexProvider
import ru.nstu.isma.compiler.hsm.jvm.calcmodel.HybridSystem

data class HsmCompilationResult(
    val indexProvider: EquationIndexProvider,
    val hybridSystem: HybridSystem,
    val classLoader: ClassLoader,
)