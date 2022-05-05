package ru.nstu.grin.concatenation.canvas.model

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import ru.nstu.grin.concatenation.axis.model.ConcatenationAxis
import ru.nstu.grin.concatenation.cartesian.model.CartesianSpace
import ru.nstu.grin.concatenation.description.model.Description
import ru.nstu.grin.concatenation.function.model.ConcatenationFunction

class ConcatenationCanvasModel {
    val cartesianSpaces = mutableListOf<CartesianSpace>()

    val functions get() = cartesianSpaces.map { it.functions }.flatten()

    val axes get() = cartesianSpaces.map { listOf(it.xAxis, it.yAxis) }.flatten()

    val descriptions get() = cartesianSpaces.map { it.descriptions }.flatten()

    private val functionsListUpdatedEventInternal = MutableSharedFlow<List<ConcatenationFunction>>()
    val functionsListUpdatedEvent = functionsListUpdatedEventInternal.asSharedFlow()

    private val axesListUpdatedEventInternal = MutableSharedFlow<List<ConcatenationAxis>>()
    val axesListUpdatedEvent = axesListUpdatedEventInternal.asSharedFlow()

    private val cartesianSpacesListUpdatedEventInternal = MutableSharedFlow<List<CartesianSpace>>()
    val cartesianSpacesListUpdatedEvent = cartesianSpacesListUpdatedEventInternal.asSharedFlow()

    private val descriptionsListUpdatedEventInternal = MutableSharedFlow<List<Description>>()
    val descriptionsListUpdatedEvent = descriptionsListUpdatedEventInternal.asSharedFlow()

    suspend fun reportFunctionsListUpdate() =
        functionsListUpdatedEventInternal.emit(functions)

    suspend fun reportAxesListUpdate() =
        axesListUpdatedEventInternal.emit(axes)

    suspend fun reportCartesianSpacesListUpdate() =
        cartesianSpacesListUpdatedEventInternal.emit(cartesianSpaces)

    suspend fun reportDescriptionsListUpdate() =
        descriptionsListUpdatedEventInternal.emit(descriptions)
}