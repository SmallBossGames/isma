package ru.nstu.grin.concatenation.points.model

import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleListProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.collections.FXCollections
import ru.nstu.grin.common.model.WaveletDirection
import ru.nstu.grin.common.model.WaveletTransformFun
import tornadofx.*

class PointsViewModel : ViewModel() {
    val pointsListProperty: SimpleListProperty<List<String>> = SimpleListProperty(FXCollections.observableArrayList())
    val pointsList by pointsListProperty

    val addFunctionsModeProperty = SimpleObjectProperty<AddFunctionsMode>()
    var addFunctionsMode by addFunctionsModeProperty

    var waveletTransformFunProperty = SimpleObjectProperty<WaveletTransformFun>()
    var waveletTransformFun by waveletTransformFunProperty

    var isWaveletProperty = SimpleBooleanProperty()
    var isWavelet: Boolean by isWaveletProperty

    var waveletDirectionProperty = SimpleObjectProperty<WaveletDirection>()
    var waveletDirection by waveletDirectionProperty
}