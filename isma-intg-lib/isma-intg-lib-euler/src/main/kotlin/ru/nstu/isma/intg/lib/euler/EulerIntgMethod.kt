package ru.nstu.isma.intg.lib.euler

import ru.nstu.isma.intg.api.methods.IIntegrationMethodFactory
import ru.nstu.isma.intg.api.methods.IntegrationMethodRungeKutta

class IntegrationMethodFactory : IIntegrationMethodFactory{
    override val name = "Euler"

    override fun create() = IntegrationMethodRungeKutta(
        nextY = { step, k, y, f -> y + step * f }
    )
}