package ru.nstu.isma.next.core.sim.controller.services

import ru.nstu.isma.intg.api.methods.IntgMethod

interface IIntegrationMethodProvider {
    val method: IntgMethod
}