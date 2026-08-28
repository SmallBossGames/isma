package ru.isma.next.app.models

data class CompilationErrorItem(
    val row: Int,
    val position: Int,
    val fragmentName: String,
    val message: String
)
