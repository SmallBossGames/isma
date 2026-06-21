package ru.nstu.grin.concatenation.points.events

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow

object EventBus {
    private val _events = MutableSharedFlow<FileCheckedEvent>(extraBufferCapacity = 10)
    val events: Flow<FileCheckedEvent> = _events

    fun fire(event: FileCheckedEvent) {
        _events.tryEmit(event)
    }
}
