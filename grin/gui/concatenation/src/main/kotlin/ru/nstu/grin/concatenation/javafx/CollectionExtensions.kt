package ru.nstu.grin.concatenation.javafx

import javafx.collections.ListChangeListener
import javafx.collections.ObservableList
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch

fun <T> ObservableList<T>.changeAsFlow(coroutineScope: CoroutineScope) = callbackFlow {
    val listener = ListChangeListener<T> {
        coroutineScope.launch {
            send(it)
        }
    }

    addListener(listener)

    awaitClose {
        removeListener(listener)
    }
}