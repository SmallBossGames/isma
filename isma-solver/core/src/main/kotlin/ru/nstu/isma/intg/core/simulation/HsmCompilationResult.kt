package ru.nstu.isma.intg.core.simulation

import ru.nstu.isma.compiler.hsm.jvm.EquationIndexProvider
import ru.nstu.isma.compiler.hsm.jvm.calcmodel.HybridSystem

data class HsmCompilationResult(
    val indexProvider: EquationIndexProvider,
    val hybridSystem: HybridSystem,
)
