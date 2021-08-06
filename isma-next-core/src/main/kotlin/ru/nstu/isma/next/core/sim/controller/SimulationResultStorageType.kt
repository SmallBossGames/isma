package ru.nstu.isma.next.core.sim.controller

sealed interface SimulationResultStorageType

object MemoryStorage : SimulationResultStorageType
data class FileStorage(val filePath: String) : SimulationResultStorageType
