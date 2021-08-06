package ru.nstu.isma.intg.api

import java.util.function.Consumer
import kotlinx.coroutines.flow.Flow

interface IntgResultPointProvider {
    val results: Flow<IntgResultPoint>
}