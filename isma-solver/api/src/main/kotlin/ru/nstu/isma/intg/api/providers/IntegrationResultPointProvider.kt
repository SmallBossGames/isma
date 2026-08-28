package ru.nstu.isma.intg.api.providers

import kotlinx.coroutines.flow.Flow
import ru.nstu.isma.intg.api.models.IntgResultPoint

interface IntegrationResultPointProvider {
    val results: Flow<IntgResultPoint>
}