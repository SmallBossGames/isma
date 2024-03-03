package ru.nstu.isma.next.integration.services

import ru.nstu.isma.intg.api.methods.IIntegrationMethodFactory
import ru.nstu.isma.intg.api.methods.IntgMethod
import java.util.*


class IntegrationMethodLibraryLoader {
    companion object {
        @JvmStatic
        fun load(): IntegrationMethodsLibrary {
            val methods = ServiceLoader.load(IIntegrationMethodFactory::class.java)
            return IntegrationMethodsLibrary(methods)
        }
    }
}