package ru.nstu.isma.daemon.infrastructure.stores.compiledModels

import ru.nstu.isma.domain.compiler.CompiledModel
import ru.nstu.isma.domain.compiler.ICompiledModelStore
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

class CompiledModelStore : ICompiledModelStore {
    private val models = ConcurrentHashMap<String, CompiledModel>()

    override fun create(model: CompiledModel): String {
        val id = UUID.randomUUID().toString()
        models[id] = model
        return id
    }

    override fun get(id: String): CompiledModel? = models[id]

    override fun delete(id: String): Boolean = models.remove(id) != null

    override fun exists(id: String): Boolean = models.containsKey(id)
}
