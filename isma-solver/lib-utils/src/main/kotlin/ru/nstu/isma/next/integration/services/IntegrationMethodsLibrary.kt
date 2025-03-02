package ru.nstu.isma.next.integration.services

import ru.nstu.isma.intg.api.methods.IIntegrationMethodFactory

class IntegrationMethodsLibrary(methods: Iterable<IIntegrationMethodFactory>) {
    private val methods = methods.associateBy { it.name }.toMap()

    fun getIntegrationMethod(name: String) = methods[name] ?: throw UnsupportedOperationException()

    fun getIntegrationMethodNames() = methods.keys.sorted().toList()
}