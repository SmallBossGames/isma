package ru.nstu.isma.intg.core.simulation

interface IEventDetectorFactory {
    fun create(): IEventDetector?
}
