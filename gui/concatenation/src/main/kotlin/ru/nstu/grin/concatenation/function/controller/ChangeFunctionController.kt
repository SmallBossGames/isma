package ru.nstu.grin.concatenation.function.controller

import ru.nstu.grin.concatenation.function.events.GetFunctionEvent
import ru.nstu.grin.concatenation.function.events.UpdateFunctionEvent
import ru.nstu.grin.concatenation.function.model.ChangeFunctionModel
import ru.nstu.grin.concatenation.function.model.MirrorSettings
import tornadofx.Controller
import java.util.*

class ChangeFunctionController : Controller() {
    val functionId: UUID by param()
    private val model: ChangeFunctionModel by inject()

    init {
        subscribe<GetFunctionEvent> {
            if (it.function.id == functionId) {
                model.name = it.function.name
                model.functionColor = it.function.functionColor
                model.lineSize = it.function.lineSize.toString()
                model.lineType = it.function.lineType
                model.isHide = !it.function.isHide
                model.isMirrorX = it.function.mirrorSettings.isMirrorX
                model.isMirrorY = it.function.mirrorSettings.isMirrorY
            }
        }
    }

    fun updateFunction() {
        val event = UpdateFunctionEvent(
            id = functionId,
            name = model.name,
            color = model.functionColor,
            lineType = model.lineType,
            lineSize = model.lineSize.toDouble(),
            isHide = !model.isHide,
            mirrorSettings = MirrorSettings(isMirrorX = model.isMirrorX, isMirrorY = model.isMirrorY)
        )
        fire(event)
    }
}