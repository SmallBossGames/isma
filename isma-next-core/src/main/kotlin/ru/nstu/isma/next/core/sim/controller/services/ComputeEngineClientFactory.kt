package ru.nstu.isma.next.core.sim.controller.services

import ru.nstu.isma.intg.api.methods.IntgMethod
import ru.nstu.isma.intg.server.client.ComputeEngineClient
import ru.nstu.isma.next.core.sim.controller.models.HsmCompilationResult

class ComputeEngineClientFactory {
    fun create(server: String, port: Int, method: IntgMethod, compilationResult: HsmCompilationResult): ComputeEngineClient {
        return ComputeEngineClient(compilationResult.classLoader).apply {
            connect(server, port)
            loadIntgMethod(method)
            loadDaeSystem(compilationResult.hybridSystem.daeSystem)
        }
    }
}