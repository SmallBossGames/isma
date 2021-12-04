package ru.nstu.isma.next.core.sim.controller.services.hsm

import ru.nstu.isma.core.hsm.HSM
import ru.nstu.isma.next.core.sim.controller.models.HsmCompilationResult

interface IHsmCompiler {
    suspend fun compile(hsm: HSM): HsmCompilationResult
}
