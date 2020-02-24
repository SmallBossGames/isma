package ru.nstu.grin.events.concatenation

import tornadofx.FXEvent
import java.io.File

class SaveEvent(val file: File) : FXEvent()