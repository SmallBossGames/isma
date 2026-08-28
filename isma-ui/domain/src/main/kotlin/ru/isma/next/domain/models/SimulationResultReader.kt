package ru.isma.next.domain.models

import kotlinx.coroutines.flow.Flow

interface SimulationResultReader {
    val results: Flow<SimulationPoint>
}
