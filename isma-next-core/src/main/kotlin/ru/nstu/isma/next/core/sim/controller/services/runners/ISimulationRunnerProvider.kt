package ru.nstu.isma.next.core.sim.controller.services.runners

interface ISimulationRunnerProvider {
    fun create(): ISimulationRunner
}