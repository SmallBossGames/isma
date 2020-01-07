package ru.nstu.grin.controller.events

import tornadofx.FXEvent
import java.io.File

class LoadEvent(val file: File) : FXEvent()