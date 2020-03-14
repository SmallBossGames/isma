package ru.nstu.grin.concatenation.events

import tornadofx.FXEvent
import java.io.File

class SaveEvent(val file: File) : FXEvent()