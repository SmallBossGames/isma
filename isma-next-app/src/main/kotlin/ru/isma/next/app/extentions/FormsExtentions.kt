package ru.isma.next.app.extentions

import javafx.beans.property.DoubleProperty
import javafx.beans.property.IntegerProperty
import javafx.scene.control.TextField
import javafx.util.StringConverter
import tornadofx.Field
import tornadofx.bind
import tornadofx.onChange
import tornadofx.textfield

private class DoubleConverter(): StringConverter<Number>() {
    override fun toString(value: Number?): String {
        return value?.toString() ?: "0"
    }

    override fun fromString(string: String?): Number {
        return string?.toDoubleOrNull() ?: 0.0
    }
}

private class IntegerConverter(): StringConverter<Number>() {
    override fun toString(value: Number?): String {
        return value?.toString() ?: "0"
    }

    override fun fromString(string: String?): Number {
        return string?.toIntOrNull() ?: 0
    }
}

private val doubleConverterInstance = DoubleConverter()
private val integerConverterInstance = IntegerConverter()

private fun StringConverter<Number>.normalizeString(string: String?) : String {
    val parsedValue = fromString(string)
    return toString(parsedValue)
}

fun TextField.bindDouble(property: DoubleProperty) = bind(property, converter = doubleConverterInstance)

fun TextField.bindInteger(property: IntegerProperty) = bind(property, converter = integerConverterInstance)

fun Field.numberTextField(property: DoubleProperty): TextField {
    return textfield {
        bindDouble(property)
        focusedProperty().onChange {
            if (!it) {
                text = doubleConverterInstance.normalizeString(text)
            }
        }
    }
}

inline fun Field.numberTextField(property: DoubleProperty, op: TextField.() -> Unit){
    op(numberTextField(property))
}

fun Field.integerTextField(property: IntegerProperty) : TextField {
    return textfield {
        bindInteger(property)
        focusedProperty().onChange {
            if (!it) {
                text = integerConverterInstance.normalizeString(text)
            }
        }
    }
}

inline fun Field.integerTextField(property: IntegerProperty, op: TextField.() -> Unit){
    op(integerTextField(property))
}