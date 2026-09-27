package ru.nstu.isma.daemon.infrastructure.stores.integrationMethods

import ru.nstu.isma.domain.integration.IIntegrationMethodsStore
import ru.nstu.isma.next.integration.services.IntegrationMethodsLibrary

class IntegrationMethodsStore(
    private val library: IntegrationMethodsLibrary,
) : IIntegrationMethodsStore {
    override fun getMethodNames(): List<String> = library.getIntegrationMethodNames()
}
