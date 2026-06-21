package ru.nstu.grin.concatenation.points.model

import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleListProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import ru.nstu.grin.common.model.WaveletDirection
import ru.nstu.grin.common.model.WaveletTransformFun
import ru.isma.javafx.extensions.helpers.getValue
import ru.isma.javafx.extensions.helpers.setValue

class PointsModel {
    val pointsListProperty: SimpleListProperty<List<String>> = SimpleListProperty(FXCollections.observableArrayList())
    val pointsList: ObservableList<List<String>> get() = pointsListProperty.value

    val addFunctionsModeProperty = SimpleObjectProperty<AddFunctionsMode>()
    var addFunctionsMode: AddFunctionsMode
        get() = addFunctionsModeProperty.value!!
        set(value) { addFunctionsModeProperty.value = value }

    var waveletTransformFunProperty = SimpleObjectProperty<WaveletTransformFun>()
    var waveletTransformFun: WaveletTransformFun?
        get() = waveletTransformFunProperty.value
        set(value) { waveletTransformFunProperty.value = value }

    var isWaveletProperty = SimpleBooleanProperty()
    var isWavelet: Boolean
        get() = isWaveletProperty.value
        set(value) { isWaveletProperty.value = value }

    var waveletDirectionProperty = SimpleObjectProperty<WaveletDirection>()
    var waveletDirection: WaveletDirection?
        get() = waveletDirectionProperty.value
        set(value) { waveletDirectionProperty.value = value }
}