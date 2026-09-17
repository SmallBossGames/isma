package ru.nstu.isma.intg.api.providers

import ru.nstu.isma.intg.api.methods.IIntegrationMethod

interface IIntegrationMethodProvider {
    val method: IIntegrationMethod
}