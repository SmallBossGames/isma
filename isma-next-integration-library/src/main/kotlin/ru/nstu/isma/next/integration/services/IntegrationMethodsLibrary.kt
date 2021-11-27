package ru.nstu.isma.next.integration.services

import ru.nstu.isma.intg.api.methods.IntgMethod

class IntegrationMethodsLibrary(methods: Iterable<IntgMethod>) {
    private val methods = methods.associateBy { it.name }.toMap()

    fun getIntegrationMethod(name: String) = methods[name]

    fun getIntegrationMethodNames() = methods.keys.sorted().toList()
}