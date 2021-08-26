package ru.nstu.isma.next.integration.services

import ru.nstu.isma.intg.api.methods.IntgMethod
import java.util.stream.Collectors

class IntegrationMethodsLibrary {
    private val systemMethods = HashMap<String, IntgMethod>()

    fun getIntgMethod(name: String): IntgMethod? {
        return systemMethods[name]
    }

    fun containsIntgMethod(name: String): Boolean {
        return getIntgMethod(name) != null
    }

    fun getIntgMethodNames(): List<String>? {
        val methodNames = systemMethods.keys.stream()
            .sorted()
            .collect(Collectors.toList())
        return methodNames
    }

    fun registerIntgMethod(intgMethod: IntgMethod) {
        val intgMethodName = intgMethod.name
        require(getIntgMethod(intgMethodName) == null) { "Integration method with name \"$intgMethodName\" is already registered." }
        systemMethods[intgMethodName] = intgMethod
    }
}