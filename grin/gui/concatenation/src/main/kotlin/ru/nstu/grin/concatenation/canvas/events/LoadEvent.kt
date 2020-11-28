package ru.nstu.grin.concatenation.canvas.events

import tornadofx.FXEvent
import java.io.File

class LoadEvent(val file: File) : FXEvent()