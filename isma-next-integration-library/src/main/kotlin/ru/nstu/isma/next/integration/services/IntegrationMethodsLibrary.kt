package ru.nstu.isma.next.integration.services

import ru.nstu.isma.intg.api.methods.IntgMethod
import java.util.stream.Collectors

class IntegrationMethodsLibrary {
    private val systemMethods = HashMap<String, IntgMethod>()
    private val userMethods = HashMap<String, IntgMethod>()

    fun getIntgMethod(name: String): IntgMethod? {
        var intgMethod = systemMethods[name]
        if (intgMethod == null) {
            intgMethod = userMethods[name]
        }
        return intgMethod
    }

    fun isSystemIntgMethod(name: String): Boolean {
        return systemMethods.containsKey(name)
    }

    fun containsIntgMethod(name: String): Boolean {
        return getIntgMethod(name) != null
    }

    fun getIntgMethodNames(): List<String>? {
        val methodNames = systemMethods.keys.stream()
            .sorted()
            .collect(Collectors.toList())
        val userMethodNames = userMethods.keys.stream()
            .sorted()
            .collect(Collectors.toList())
        methodNames.addAll(userMethodNames)
        return methodNames
    }

    fun registerIntgMethod(intgMethod: IntgMethod, system: Boolean) {
        val intgMethodName = intgMethod.name
        require(getIntgMethod(intgMethodName) == null) { "Integration method with name \"$intgMethodName\" is already registered." }
        if (system) {
            systemMethods[intgMethodName] = intgMethod
        } else {
            userMethods[intgMethodName] = intgMethod
        }
    }
}