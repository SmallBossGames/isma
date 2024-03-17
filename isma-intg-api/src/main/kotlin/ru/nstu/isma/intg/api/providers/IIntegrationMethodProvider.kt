package ru.nstu.isma.intg.api.providers

import ru.nstu.isma.intg.api.methods.IntegrationMethodRungeKutta

interface IIntegrationMethodProvider {
    val method: IntegrationMethodRungeKutta
}