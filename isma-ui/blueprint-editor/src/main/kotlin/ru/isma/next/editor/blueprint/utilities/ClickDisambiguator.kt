package ru.isma.next.editor.blueprint.utilities

import javafx.animation.KeyFrame
import javafx.animation.Timeline
import javafx.scene.input.MouseEvent
import javafx.util.Duration

class ClickDisambiguator(
    private val singleClick: (MouseEvent) -> Unit,
    private val doubleClick: (MouseEvent) -> Unit,
    private val clickDelay: Long = 200L,
) {
    private var pendingTimeline: Timeline? = null
    private var lastEvent: MouseEvent? = null
    private var isDragged = false

    fun onKeyPress() {
        isDragged = false
    }

    fun onDragged() {
        isDragged = true
    }

    fun onClick(event: MouseEvent) {
        if (isDragged) return
        lastEvent = event
        when (event.clickCount) {
            1 -> handleSingleClick()
            2 -> handleDoubleClick()
        }
    }

    fun cancel() {
        pendingTimeline?.stop()
        pendingTimeline = null
        lastEvent = null
    }

    private fun handleSingleClick() {
        pendingTimeline?.stop()
        val timeline = Timeline(
            KeyFrame(Duration.millis(clickDelay.toDouble()), {
                pendingTimeline = null
                val event = lastEvent
                lastEvent = null
                if (!isDragged && event != null) singleClick(event)
            })
        )
        timeline.cycleCount = 1
        timeline.play()
        pendingTimeline = timeline
    }

    private fun handleDoubleClick() {
        pendingTimeline?.stop()
        pendingTimeline = null
        doubleClick(lastEvent!!)
    }
}
