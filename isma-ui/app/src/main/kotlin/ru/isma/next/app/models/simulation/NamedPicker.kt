package ru.isma.next.app.models.simulation

data class NamedPickerItem<T>(val name: String, val value: T)

data class NamedPickerModel<T>(
    val xAxisItem: NamedPickerItem<T>,
    val yAxisItems: List<NamedPickerItem<T>>
)
