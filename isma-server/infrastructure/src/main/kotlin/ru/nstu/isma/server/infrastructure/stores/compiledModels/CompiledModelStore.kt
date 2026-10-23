package ru.nstu.isma.server.infrastructure.stores.compiledModels

import ru.nstu.isma.compiler.hsm.core.IHSM
import ru.nstu.isma.domain.compiler.ICompiledModelStore
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

class CompiledModelStore : ICompiledModelStore {
    private val models = ConcurrentHashMap<String, IHSM>()

    override fun create(hsm: IHSM): String {
        val id = UUID.randomUUID().toString()
        models[id] = hsm
        return id
    }

    override fun get(id: String): IHSM? = models[id]

    override fun delete(id: String): Boolean = models.remove(id) != null

    override fun exists(id: String): Boolean = models.containsKey(id)
}
