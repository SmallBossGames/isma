package ru.nstu.grin.concatenation.function.controller

import ru.nstu.grin.concatenation.function.events.GetAllFunctionsEvent
import ru.nstu.grin.concatenation.function.events.GetAllFunctionsQuery
import ru.nstu.grin.concatenation.function.events.UpdateFunctionEvent
import ru.nstu.grin.concatenation.function.model.ConcatenationFunction
import ru.nstu.grin.concatenation.function.model.MirrorFunctionModel
import tornadofx.Controller

class MirrorFunctionController : Controller() {
    private val model: MirrorFunctionModel by inject()

    init {
        subscribe<GetAllFunctionsEvent> {
            if (model.functions != null) {
                model.functions.clear()
            }
            model.functionsProperty.setAll(it.functions)
        }
    }

    fun getAllFunctions() {
        fire(GetAllFunctionsQuery())
    }

    fun mirrorFunction(isY: Boolean, function: ConcatenationFunction) {
        val mirrorEvent = UpdateFunctionEvent(
            id = function.id,
            name = function.name,
            color = function.functionColor,
            lineType = function.lineType,
            lineSize = function.lineSize,
            isHide = function.isHide,
            mirrorSettings = if (isY) {
                function.mirrorSettings.copy(
                    isMirrorY = !function.mirrorSettings.isMirrorY
                )
            } else {
                function.mirrorSettings.copy(
                    isMirrorX = !function.mirrorSettings.isMirrorX
                )
            }
        )
        fire(mirrorEvent)
    }

}