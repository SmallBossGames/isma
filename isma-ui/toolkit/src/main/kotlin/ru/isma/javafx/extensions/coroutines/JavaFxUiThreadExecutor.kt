package ru.isma.javafx.extensions.coroutines

import javafx.application.Platform

class JavaFxUiThreadExecutor : UiThreadExecutor {
    override fun executeOnUi(runnable: () -> Unit) {
        Platform.runLater(runnable)
    }
}
