package ru.nstu.isma.next.core.sim.controller.services

interface ISimulationRunnerFactory {
    fun create(): ISimulationRunner
}