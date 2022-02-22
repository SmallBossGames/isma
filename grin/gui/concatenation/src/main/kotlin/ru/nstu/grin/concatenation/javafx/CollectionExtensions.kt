package ru.nstu.grin.concatenation.javafx

import javafx.collections.ListChangeListener
import javafx.collections.ObservableList
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch

suspend fun <T> ObservableList<T>.changeAsFlow() = callbackFlow {
    val listener = ListChangeListener<T> {
        launch {
            send(it)
        }
    }

    addListener(listener)

    awaitClose {
        removeListener(listener)
    }
}