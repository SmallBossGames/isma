package ru.nstu.grin.controller.events

import tornadofx.FXEvent
import java.io.File

class SaveEvent(val file: File) : FXEvent()