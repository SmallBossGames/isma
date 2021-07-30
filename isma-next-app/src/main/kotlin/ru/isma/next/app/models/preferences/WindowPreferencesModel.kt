package ru.isma.next.app.models.preferences

import kotlinx.serialization.Serializable

@Serializable
data class WindowPreferencesModel(
    val height: Double = 0.0,
    val width: Double = 0.0,
    val isMaximized: Boolean = false,
    val x: Double = 0.0,
    val y: Double = 0.0,
)