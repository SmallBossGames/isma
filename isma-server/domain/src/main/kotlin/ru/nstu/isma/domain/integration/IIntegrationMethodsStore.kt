package ru.nstu.isma.domain.integration

import ru.nstu.isma.intg.api.methods.IIntegrationMethodFactory

interface IIntegrationMethodsStore {
    fun getMethodNames(): List<String>
    fun getMethod(name: String): IIntegrationMethodFactory
}
