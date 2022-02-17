package ru.isma.next.app.javafx

import javafx.collections.ListChangeListener
import javafx.collections.ObservableList
import javafx.collections.ObservableSet
import javafx.collections.SetChangeListener
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch

fun <T> ObservableSet<T>.addedAsFlow(coroutineScope: CoroutineScope) = callbackFlow {
    val listener = SetChangeListener<T?> {
        val element = it.elementAdded ?: return@SetChangeListener

        coroutineScope.launch {
            send(element)
        }
    }

    addListener (listener)

    awaitClose {
        removeListener(listener)
    }
}

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

