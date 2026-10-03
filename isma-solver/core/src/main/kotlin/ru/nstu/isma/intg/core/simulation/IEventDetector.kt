package ru.nstu.isma.intg.core.simulation

import ru.nstu.isma.compiler.hsm.jvm.calcmodel.Guard
import ru.nstu.isma.intg.api.methods.IntgPoint

interface IEventDetector {
    fun predictNextStep(point: IntgPoint, guards: List<Guard>): Double
}
