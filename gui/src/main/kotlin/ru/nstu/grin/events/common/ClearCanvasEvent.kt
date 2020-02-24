package ru.nstu.grin.events.common

import tornadofx.FXEvent

sealed class ClearCanvasEvent : FXEvent()

object SimpleClearCanvasEvent : ClearCanvasEvent()

object ConcatenationClearCanvasEvent : ClearCanvasEvent()