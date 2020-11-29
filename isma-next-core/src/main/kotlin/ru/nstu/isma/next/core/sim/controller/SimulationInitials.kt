package ru.nstu.isma.next.core.sim.controller

/**
 * @author Maria Nasyrova
 * @since 06.10.2015
 */
class SimulationInitials(
        val differentialEquationInitials: DoubleArray,
        val step: Double,
        val start: Double,
        val end: Double
)