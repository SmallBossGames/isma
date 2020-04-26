package ru.nstu.grin.concatenation.model

import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleListProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.collections.FXCollections
import ru.nstu.grin.common.model.WaveletDirection
import ru.nstu.grin.common.model.WaveletTransformFun
import tornadofx.*
import java.io.File

class PointsViewModel : ViewModel() {
    val pointsListProperty: SimpleListProperty<List<String>> = SimpleListProperty(FXCollections.observableArrayList())
    val pointsList by pointsListProperty

    val addFunctionsModeProperty = SimpleObjectProperty<AddFunctionsMode>()
    var addFunctionsMode by addFunctionsModeProperty

    val file: File by param()
    val delimiter: String by param()
    val readerMode: FileReaderMode by param()

    var waveletTransformFunProperty = SimpleObjectProperty<WaveletTransformFun>()
    var waveletTransformFun by waveletTransformFunProperty

    var isWaveletProperty = SimpleBooleanProperty()
    var isWavelet: Boolean by isWaveletProperty

    var waveletDirectionProperty = SimpleObjectProperty<WaveletDirection>()
    var waveletDirection by waveletDirectionProperty
}