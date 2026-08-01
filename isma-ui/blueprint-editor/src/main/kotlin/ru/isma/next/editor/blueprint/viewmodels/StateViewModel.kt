package ru.isma.next.editor.blueprint.viewmodels

import javafx.beans.binding.Bindings
import javafx.beans.binding.DoubleBinding
import javafx.beans.property.*
import javafx.scene.paint.Color
import javafx.scene.paint.Paint
import ru.isma.next.editor.blueprint.constants.DEFAULT_STATE_HEIGHT
import ru.isma.next.editor.blueprint.constants.DEFAULT_STATE_WIDTH

class StateViewModel(
    name: String = "",
    text: String = "",
    x: Double = 0.0,
    y: Double = 0.0,
    squareWidth: Double = DEFAULT_STATE_WIDTH,
    squareHeight: Double = DEFAULT_STATE_HEIGHT,
    color: Paint = Color.WHITE,
    editable: Boolean = true,
    editMode: Boolean = false,
    editButtonVisible: Boolean = true,
    val isNameUnique: (String) -> Boolean = { true }
) {
    val nameProperty = SimpleStringProperty(name)
    val textProperty = SimpleStringProperty(text)
    val xProperty = SimpleDoubleProperty(x)
    val yProperty = SimpleDoubleProperty(y)
    val squareWidthProperty = SimpleDoubleProperty(squareWidth)
    val squareHeightProperty = SimpleDoubleProperty(squareHeight)
    val colorProperty = SimpleObjectProperty<Paint>(color)
    val editableProperty = SimpleBooleanProperty(editable)
    val editModeProperty = SimpleBooleanProperty(editMode)
    val editButtonVisibleProperty = SimpleBooleanProperty(editButtonVisible)

    var name: String
        get() = nameProperty.value
        set(value) {
            if (value == nameProperty.value) return
            if (!isNameUnique(value)) {
                return
            }
            nameProperty.value = value
        }

    var text: String
        get() = textProperty.value
        set(value) {
            textProperty.value = value
        }

    var x: Double
        get() = xProperty.value
        set(value) {
            xProperty.value = value
        }

    var y: Double
        get() = yProperty.value
        set(value) {
            yProperty.value = value
        }

    var squareWidth: Double
        get() = squareWidthProperty.value
        set(value) {
            squareWidthProperty.value = value
        }

    var squareHeight: Double
        get() = squareHeightProperty.value
        set(value) {
            squareHeightProperty.value = value
        }

    var color: Paint
        get() = colorProperty.value
        set(value) {
            colorProperty.value = value
        }

    var editable: Boolean
        get() = editableProperty.value
        set(value) {
            editableProperty.value = value
        }

    var editMode: Boolean
        get() = editModeProperty.value
        set(value) {
            editModeProperty.value = value
        }

    @Suppress("UNCHECKED_CAST")
    fun centerX(): DoubleBinding = Bindings.add(xProperty, squareWidthProperty.divide(2.0)) as DoubleBinding

    @Suppress("UNCHECKED_CAST")
    fun centerY(): DoubleBinding = Bindings.add(yProperty, squareHeightProperty.divide(2.0)) as DoubleBinding

    fun startEdit() {
        editMode = true
    }

    fun commitEdit() {
        editMode = false
    }

    fun cancelEdit() {
        editMode = false
    }
}
