package ru.nstu.grin.concatenation.events

import tornadofx.FXEvent
import java.io.File

class LoadEvent(val file: File) : FXEvent()