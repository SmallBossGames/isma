package ru.nstu.isma.next.core.sim.controller.services.eventDetection

interface IEventDetectorFactory {
    fun create(): IEventDetector?
}