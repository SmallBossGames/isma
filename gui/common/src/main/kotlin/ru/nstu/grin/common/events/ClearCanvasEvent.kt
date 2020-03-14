package ru.nstu.grin.common.events

import tornadofx.FXEvent

sealed class ClearCanvasEvent : FXEvent()

object SimpleClearCanvasEvent : ClearCanvasEvent()

object ConcatenationClearCanvasEvent : ClearCanvasEvent()