package ru.nstu.grin.concatenation.canvas.events

import tornadofx.FXEvent
import java.io.File

class SaveEvent(val file: File) : FXEvent()