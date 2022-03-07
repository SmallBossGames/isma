package ru.nstu.isma.next.core.sim.controller.services.solvers

import ru.nstu.isma.intg.api.methods.IntgMethod
import ru.nstu.isma.intg.api.solvers.DaeSystemStepSolver
import ru.nstu.isma.intg.server.client.ComputeEngineClient
import ru.nstu.isma.intg.server.client.RemoteDaeSystemStepSolver
import ru.nstu.isma.next.core.sim.controller.models.HsmCompilationResult

class RemoteDaeSystemStepSolverFactory {
    fun create(server: String, port: Int, method: IntgMethod, compilationResult: HsmCompilationResult)
    : RemoteDaeSystemStepSolver {
        val client = ComputeEngineClient(compilationResult.classLoader).apply {
            connect(server, port)
            loadIntgMethod(method)
            loadDaeSystem(compilationResult.hybridSystem.daeSystem)
        }

        return RemoteDaeSystemStepSolver(method, client)
    }
}