package ru.nstu.grin.concatenation.canvas.model

import javafx.collections.FXCollections.observableArrayList
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import ru.nstu.grin.common.model.Arrow
import ru.nstu.grin.common.model.Description
import ru.nstu.grin.concatenation.axis.model.ConcatenationAxis
import ru.nstu.grin.concatenation.cartesian.model.CartesianSpace
import ru.nstu.grin.concatenation.function.model.ConcatenationFunction
import ru.nstu.grin.concatenation.points.model.PointToolTipsSettings

class ConcatenationCanvasModel {
    val cartesianSpaces = observableArrayList<CartesianSpace>()!!

    val arrows = observableArrayList<Arrow>()!!

    val descriptions = observableArrayList<Description>()!!

    val pointToolTipSettings = PointToolTipsSettings(false, mutableSetOf())

    val selectionSettings = SelectionSettings()

    var traceSettings: TraceSettings? = null

    var moveSettings: MoveSettings? = null

    val selectedFunction get() = cartesianSpaces.map { it.functions }.flatten().firstOrNull { it.isSelected }

    val selectedDescription get() = descriptions.firstOrNull { it.isSelected }

    private val functionsListUpdatedEventInternal = MutableSharedFlow<List<ConcatenationFunction>>()
    val functionsListUpdatedEvent = functionsListUpdatedEventInternal.asSharedFlow()

    private val axesListUpdatedEventInternal = MutableSharedFlow<List<ConcatenationAxis>>()
    val axesListUpdatedEvent = axesListUpdatedEventInternal.asSharedFlow()

    private val cartesianSpacesListUpdatedEventInternal = MutableSharedFlow<List<CartesianSpace>>()
    val cartesianSpacesListUpdatedEvent = cartesianSpacesListUpdatedEventInternal.asSharedFlow()

    private val descriptionsListUpdatedEventInternal = MutableSharedFlow<List<Description>>()
    val descriptionsListUpdatedEvent = descriptionsListUpdatedEventInternal.asSharedFlow()

    fun getAllFunctions() = cartesianSpaces.map { it.functions }.flatten()

    fun getAllAxes() = cartesianSpaces.map { listOf(it.xAxis, it.yAxis) }.flatten()

    fun getAllCartesianSpaces() = cartesianSpaces

    fun getAllDescriptions() = descriptions

    suspend fun reportFunctionsListUpdate() =
        functionsListUpdatedEventInternal.emit(getAllFunctions())

    suspend fun reportAxesListUpdate() =
        axesListUpdatedEventInternal.emit(getAllAxes())

    suspend fun reportCartesianSpacesListUpdate() =
        cartesianSpacesListUpdatedEventInternal.emit(getAllCartesianSpaces())

    suspend fun reportDescriptionsListUpdate() =
        descriptionsListUpdatedEventInternal.emit(getAllDescriptions())

    suspend fun reportUpdateAll(){
        reportFunctionsListUpdate()
        reportAxesListUpdate()
        reportCartesianSpacesListUpdate()
        reportDescriptionsListUpdate()
    }
}