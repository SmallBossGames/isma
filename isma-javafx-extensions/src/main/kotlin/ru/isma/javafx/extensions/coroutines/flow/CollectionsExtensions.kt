package ru.isma.javafx.extensions.coroutines.flow

import javafx.collections.ListChangeListener
import javafx.collections.ObservableList
import javafx.collections.ObservableSet
import javafx.collections.SetChangeListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch

suspend fun <T> ObservableSet<T>.addedAsFlow() = callbackFlow {
    val listener = SetChangeListener<T?> {
        val element = it.elementAdded ?: return@SetChangeListener

        launch {
            send(element)
        }
    }

    addListener (listener)

    awaitClose {
        removeListener(listener)
    }
}

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

