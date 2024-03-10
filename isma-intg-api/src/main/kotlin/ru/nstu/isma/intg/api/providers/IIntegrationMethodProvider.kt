package ru.nstu.isma.intg.api.providers

import ru.nstu.isma.intg.api.methods.IntegrationMethodRungeKutta
import ru.nstu.isma.intg.api.methods.IntgMethod

interface IIntegrationMethodProvider {
    val method: IntegrationMethodRungeKutta
}