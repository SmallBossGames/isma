package ru.nstu.isma.next.core.sim.controller.services.eventDetection

import ru.nstu.isma.intg.api.calcmodel.EventFunctionGroup
import ru.nstu.isma.intg.api.calcmodel.Guard
import ru.nstu.isma.intg.api.methods.IntgPoint

interface IEventDetector {
    fun predictNextStep(point: IntgPoint, guards: List<Guard>): Double
}