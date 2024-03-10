package ru.nstu.isma.intg.lib.euler

import ru.nstu.isma.intg.api.methods.IIntegrationMethodFactory
import ru.nstu.isma.intg.api.methods.IntegrationMethodRungeKutta
import ru.nstu.isma.intg.api.methods.IntgMethod
import ru.nstu.isma.intg.api.methods.StageCalculator

class IntegrationMethodFactory : IIntegrationMethodFactory{
    override val name = "Euler"

    override fun create() = IntegrationMethodRungeKutta(
        nextY = { step, k, y, f -> y + step * f }
    )
}