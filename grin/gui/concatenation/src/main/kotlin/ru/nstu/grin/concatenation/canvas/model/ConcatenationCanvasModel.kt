package ru.nstu.grin.concatenation.canvas.model

import javafx.collections.FXCollections.observableArrayList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import ru.isma.javafx.extensions.coroutines.flow.changeAsFlow
import ru.nstu.grin.common.model.Arrow
import ru.nstu.grin.common.model.Description
import ru.nstu.grin.concatenation.axis.model.ConcatenationAxis
import ru.nstu.grin.concatenation.cartesian.model.CartesianSpace
import ru.nstu.grin.concatenation.function.model.ConcatenationFunction
import ru.nstu.grin.concatenation.points.model.PointToolTipsSettings

class ConcatenationCanvasModel {
    private val coroutineScope = CoroutineScope(Dispatchers.Default)

    val cartesianSpaces = observableArrayList<CartesianSpace>()!!

    val functions get() = cartesianSpaces.map { it.functions }.flatten()

    val axes get() = cartesianSpaces.map { listOf(it.xAxis, it.yAxis) }.flatten()

    val arrows = observableArrayList<Arrow>()!!

    val descriptions = observableArrayList<Description>()!!

    val pointToolTipSettings = PointToolTipsSettings(false, mutableSetOf())

    var traceSettings: TraceSettings? = null

    var moveSettings: IMoveSettings? = null

    private val functionsListUpdatedEventInternal = MutableSharedFlow<List<ConcatenationFunction>>()
    val functionsListUpdatedEvent = functionsListUpdatedEventInternal.asSharedFlow()

    private val axesListUpdatedEventInternal = MutableSharedFlow<List<ConcatenationAxis>>()
    val axesListUpdatedEvent = axesListUpdatedEventInternal.asSharedFlow()

    private val cartesianSpacesListUpdatedEventInternal = MutableSharedFlow<List<CartesianSpace>>()
    val cartesianSpacesListUpdatedEvent = cartesianSpacesListUpdatedEventInternal.asSharedFlow()

    private val descriptionsListUpdatedEventInternal = MutableSharedFlow<List<Description>>()
    val descriptionsListUpdatedEvent = descriptionsListUpdatedEventInternal.asSharedFlow()

    private val arrowsListUpdatedEventInternal = MutableSharedFlow<List<Arrow>>()
    val arrowsListUpdatedEvent = arrowsListUpdatedEventInternal.asSharedFlow()

    suspend fun reportFunctionsListUpdate() =
        functionsListUpdatedEventInternal.emit(functions)

    suspend fun reportAxesListUpdate() =
        axesListUpdatedEventInternal.emit(axes)

    suspend fun reportCartesianSpacesListUpdate() =
        cartesianSpacesListUpdatedEventInternal.emit(cartesianSpaces)

    suspend fun reportDescriptionsListUpdate() =
        descriptionsListUpdatedEventInternal.emit(descriptions)

    suspend fun reportArrowsListUpdate() =
        arrowsListUpdatedEventInternal.emit(arrows)

    init {
        coroutineScope.launch {
            cartesianSpaces.apply {
                changeAsFlow().collect {
                    reportCartesianSpacesListUpdate()
                    reportAxesListUpdate()
                    reportFunctionsListUpdate()
                }
            }
        }

        coroutineScope.launch {
            descriptions.apply {
                changeAsFlow().collect {
                    reportDescriptionsListUpdate()
                }
            }
        }

        coroutineScope.launch {
            arrows.apply {
                changeAsFlow().collect {
                    reportArrowsListUpdate()
                }
            }
        }
    }
}