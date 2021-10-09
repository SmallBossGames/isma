package ru.isma.next.app.views.controls

import javafx.beans.property.*
import javafx.beans.value.ObservableValue
import javafx.collections.ObservableList
import javafx.geometry.Insets
import javafx.scene.Node
import javafx.scene.control.CheckBox
import javafx.scene.control.ComboBox
import javafx.scene.control.Label
import javafx.scene.control.TextField
import javafx.scene.layout.GridPane
import javafx.util.StringConverter
import ru.isma.next.app.extentions.DoubleConverter
import ru.isma.next.app.extentions.IntegerConverter

class PropertiesGrid: GridPane() {
    fun addNode(title: String, property: DoubleProperty) : TextField {
        return addNode(title, doubleTextField(property))
    }

    fun addNode(title: String, property: IntegerProperty) : TextField {
        return addNode(title, intTextField(property))
    }

    fun addNode(title: String, property: StringProperty) : TextField {
        val field = TextField().apply {
            textProperty().bindBidirectional(property)
        }
        return addNode(title, field)
    }

    fun addNode(title: String, property: BooleanProperty) : CheckBox {
        val checkbox = CheckBox().apply {
            selectedProperty().bindBidirectional(property)
        }
        return addNode(title, checkbox)
    }

    fun <T> addComboBox(title: String, variants: ObservableList<T>, property: Property<T>): ComboBox<T> {
        val node = ComboBox<T>(variants).apply {
            valueProperty().bindBidirectional(property)
        }
        return addNode(title, node)
    }

    private fun <T: Node> addNode(title: String, field: T) : T {
        val row = this.rowCount
        val label = Label(title).apply {
            padding = Insets(5.0, 10.0,5.0,5.0)
        }
        field.apply {
            padding = Insets(3.0, 5.0,3.0,3.0)
        }
        add(label, 0, row)
        add(field, 1, row)

        return field
    }

    private fun doubleTextField(property: DoubleProperty): TextField {
        return TextField().apply {
            textProperty().bindBidirectional(property, doubleConverterInstance)
            focusedProperty().addListener { _, _, _ ->
                text = doubleConverterInstance.normalizeString(text)
            }
        }
    }

    private fun intTextField(property: IntegerProperty): TextField {
        return TextField().apply {
            textProperty().bindBidirectional(property, integerConverterInstance)
            focusedProperty().addListener { _, _, _ ->
                text = integerConverterInstance.normalizeString(text)
            }
        }
    }

    private fun StringConverter<Number>.normalizeString(string: String?) : String {
        val parsedValue = fromString(string)
        return toString(parsedValue)
    }

    companion object {
        private val doubleConverterInstance = DoubleConverter()
        private val integerConverterInstance = IntegerConverter()
    }
}