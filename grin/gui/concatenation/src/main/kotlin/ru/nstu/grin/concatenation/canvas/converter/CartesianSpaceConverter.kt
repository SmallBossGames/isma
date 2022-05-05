package ru.nstu.grin.concatenation.canvas.converter

import ru.nstu.grin.concatenation.canvas.dto.CartesianSpaceDTO
import ru.nstu.grin.concatenation.cartesian.model.CartesianSpace
import ru.nstu.grin.concatenation.axis.model.ConcatenationAxis
import ru.nstu.grin.concatenation.function.converter.ConcatenationFunctionConverter

object CartesianSpaceConverter {
    fun merge(source: CartesianSpaceDTO, xAxis: ConcatenationAxis, yAxis: ConcatenationAxis): CartesianSpace {
        return CartesianSpace(
            name = source.name,
            functions = source.functions.map {
                ConcatenationFunctionConverter.convert(
                    it
                )
            }.toMutableList(),
            descriptions = mutableListOf(),
            xAxis = xAxis,
            yAxis = yAxis
        )
    }
}