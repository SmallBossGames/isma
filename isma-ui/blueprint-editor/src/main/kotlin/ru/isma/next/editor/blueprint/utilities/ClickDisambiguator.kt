package ru.isma.next.editor.blueprint.utilities

import javafx.scene.input.MouseEvent
import kotlinx.coroutines.*
import kotlin.time.Duration.Companion.milliseconds

class ClickDisambiguator(
    private val coroutineScope: CoroutineScope,
    private val singleClick: (MouseEvent) -> Unit,
    private val doubleClick: (MouseEvent) -> Unit,
    private val clickDelay: Long = 200L
) {
    private var pendingSingleClick: Job? = null
    private var isDragged = false
    private var lastEvent: MouseEvent? = null

    fun onKeyPress() {
        isDragged = false
    }

    fun onDragged() {
        isDragged = true
    }

    fun onClick(event: MouseEvent) {
        lastEvent = event
        when (event.clickCount) {
            1 -> handleSingleClick()
            2 -> handleDoubleClick()
        }
    }

    fun cancel() {
        pendingSingleClick?.cancel()
        pendingSingleClick = null
        lastEvent = null
    }

    private fun handleSingleClick() {
        if (pendingSingleClick == null) {
            pendingSingleClick = coroutineScope.launch {
                delay(clickDelay.milliseconds)
                pendingSingleClick = null
                val event = lastEvent
                lastEvent = null
                if (!isDragged && event != null) singleClick(event)
            }
        }
    }

    private fun handleDoubleClick() {
        pendingSingleClick?.cancel()
        pendingSingleClick = null
        doubleClick(lastEvent!!)
    }
}
