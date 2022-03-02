package ru.isma.javafx.extensions.controls

import javafx.beans.property.*
import javafx.collections.ObservableList
import javafx.geometry.Insets
import javafx.scene.Node
import javafx.scene.control.*
import javafx.scene.layout.GridPane
import javafx.scene.paint.Color
import javafx.util.StringConverter

open class PropertiesGrid: GridPane() {
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

    fun addNode(title: String, property: SimpleObjectProperty<Color>) : ColorPicker {
        val colorPicker = ColorPicker().apply {
            valueProperty().bindBidirectional(property)
        }
        return addNode(title, colorPicker)
    }

    fun <T> addNode(title: String, variants: ObservableList<T>, property: Property<T>): ComboBox<T> {
        val node = ComboBox(variants).apply {
            valueProperty().bindBidirectional(property)
        }
        return addNode(title, node)
    }

    fun <T> addNode(
        title: String,
        variants: ObservableList<T>,
        property: Property<T>,
        cellFactory: () -> ListCell<T>
    ): ComboBox<T> {
        val node = ComboBox(variants).apply {
            buttonCell = cellFactory()
            setCellFactory{
                cellFactory()
            }
            valueProperty().bindBidirectional(property)
        }
        return addNode(title, node)
    }

    private fun <T: Node> addNode(title: String, field: T) : T {
        val row = this.rowCount
        val label = Label(title).apply {
            padding = Insets(7.0, 10.0,7.0,5.0)
        }
        field.apply {
            padding = Insets(7.0, 5.0,7.0,3.0)
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
        class DoubleConverter: StringConverter<Number>() {
            override fun toString(value: Number?): String {
                return value?.toString() ?: "0"
            }

            override fun fromString(string: String?): Number {
                return string?.toDoubleOrNull() ?: 0.0
            }
        }

        class IntegerConverter: StringConverter<Number>() {
            override fun toString(value: Number?): String {
                return value?.toString() ?: "0"
            }

            override fun fromString(string: String?): Number {
                return string?.toIntOrNull() ?: 0
            }
        }

        private val doubleConverterInstance = DoubleConverter()
        private val integerConverterInstance = IntegerConverter()
    }
}

inline fun propertiesGrid(factory: PropertiesGrid.() -> Unit) =
    PropertiesGrid().apply {
        factory()
    }

