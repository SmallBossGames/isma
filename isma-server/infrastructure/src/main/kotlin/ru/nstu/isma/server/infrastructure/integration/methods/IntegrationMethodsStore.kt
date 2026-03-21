package ru.nstu.isma.server.infrastructure.integration.methods

import ru.nstu.isma.domain.integration.IIntegrationMethodsStore
import ru.nstu.isma.intg.api.methods.IIntegrationMethodFactory
import java.util.ServiceLoader

class IntegrationMethodsStore : IIntegrationMethodsStore {
    private val methods: Map<String, IIntegrationMethodFactory>

    init {
        val loader = ServiceLoader.load(IIntegrationMethodFactory::class.java)
        methods = loader.associateBy { it.name }
    }

    override fun getMethodNames(): List<String> = methods.keys.sorted()

    override fun getMethod(name: String): IIntegrationMethodFactory =
        methods[name] ?: throw UnsupportedOperationException("Integration method '$name' not found")
}
