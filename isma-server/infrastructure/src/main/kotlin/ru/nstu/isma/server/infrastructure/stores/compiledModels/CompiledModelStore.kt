package ru.nstu.isma.server.infrastructure.stores.compiledModels

import ru.nstu.isma.compiler.hsm.core.HSM
import ru.nstu.isma.domain.compiler.ICompiledModelStore
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

class CompiledModelStore : ICompiledModelStore {
    private val models = ConcurrentHashMap<String, HSM>()

    override fun create(hsm: HSM): String {
        val id = UUID.randomUUID().toString()
        models[id] = hsm
        return id
    }

    override fun get(id: String): HSM? = models[id]

    override fun delete(id: String): Boolean = models.remove(id) != null

    override fun exists(id: String): Boolean = models.containsKey(id)
}
