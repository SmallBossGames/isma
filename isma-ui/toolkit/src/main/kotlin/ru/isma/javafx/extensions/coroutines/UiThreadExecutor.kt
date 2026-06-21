package ru.isma.javafx.extensions.coroutines

interface UiThreadExecutor {
    fun executeOnUi(runnable: () -> Unit)
}
