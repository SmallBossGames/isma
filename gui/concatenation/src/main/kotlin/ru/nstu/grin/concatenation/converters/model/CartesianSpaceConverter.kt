package ru.nstu.grin.concatenation.converters.model

import ru.nstu.grin.concatenation.dto.CartesianSpaceDTO
import ru.nstu.grin.concatenation.model.CanvasSettings
import ru.nstu.grin.concatenation.model.CartesianSpace
import ru.nstu.grin.concatenation.model.axis.ConcatenationAxis

object CartesianSpaceConverter {
    fun merge(source: CartesianSpaceDTO, xAxis: ConcatenationAxis, yAxis: ConcatenationAxis): CartesianSpace {
        return CartesianSpace(
            functions = source.functions.map { ConcatenationFunctionConverter.convert(it) }.toMutableList(),
            xAxis = xAxis,
            yAxis = yAxis,
            settings = CanvasSettings()
        )
    }
}