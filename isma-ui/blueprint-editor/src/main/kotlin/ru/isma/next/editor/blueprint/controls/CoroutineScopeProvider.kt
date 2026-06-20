package ru.isma.next.editor.blueprint.controls

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.javafx.JavaFx

object CoroutineScopeProvider {
    val scope = CoroutineScope(Dispatchers.JavaFx)

    fun cancelAll() { scope.cancel() }
}
