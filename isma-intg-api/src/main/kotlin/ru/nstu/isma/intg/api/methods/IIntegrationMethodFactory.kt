package ru.nstu.isma.intg.api.methods

interface IIntegrationMethodFactory {
    val name: String

    fun create(): IntgMethod
}