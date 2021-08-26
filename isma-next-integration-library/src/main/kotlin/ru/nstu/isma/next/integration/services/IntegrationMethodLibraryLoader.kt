package ru.nstu.isma.next.integration.services

import ru.nstu.isma.intg.api.methods.IntgMethod
import java.util.*


class IntegrationMethodLibraryLoader {
    fun load() : IntegrationMethodsLibrary {
        val libraryInstance = IntegrationMethodsLibrary()
        val systemMethods = ServiceLoader.load(IntgMethod::class.java)
        systemMethods.forEach { cls ->
            registerIntgMethod(libraryInstance, cls)
        }

        return libraryInstance
    }

    private fun registerIntgMethod(libraryInstance: IntegrationMethodsLibrary, integrationMethod: IntgMethod): Boolean {
        if (!libraryInstance.containsIntgMethod(integrationMethod.name)) {
            libraryInstance.registerIntgMethod(integrationMethod)
            return true
        }
        return false
    }

}