package ru.nstu.isma.next.core.sim.controller.services.hsm

import ru.nstu.isma.compiler.hsm.core.HSM
import ru.nstu.isma.next.core.sim.controller.models.HsmCompilationResult

interface IHsmCompiler {
    fun compile(hsm: HSM): HsmCompilationResult
}
