package ru.nstu.grin.events.concatenation

import tornadofx.FXEvent
import java.io.File

class LoadEvent(val file: File) : FXEvent()