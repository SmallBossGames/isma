package ru.nstu.grin.controller.concatenation

import ru.nstu.grin.model.Direction
import ru.nstu.grin.model.Drawable
import ru.nstu.grin.model.drawable.ConcatenationFunction
import ru.nstu.grin.model.drawable.axis.*

class PointCoefCalculator {
    fun getStartPointCoef(direction: Direction, drawings: List<Drawable>): Double {
        return when (direction) {
            Direction.LEFT -> drawings.map {
                if (it is ConcatenationFunction) {
                    when {
                        it.xAxis is LeftAxis -> {
                            it.xAxis
                        }
                        it.yAxis is LeftAxis -> {
                            it.yAxis
                        }
                    }
                }
            }.count()
            Direction.RIGHT -> {
                drawings.map {
                    if (it is ConcatenationFunction) {
                        when {
                            it.xAxis is RightAxis -> {
                                it.xAxis
                            }
                            it.yAxis is RightAxis -> {
                                it.yAxis
                            }
                        }
                    }
                }.count()
            }
            Direction.TOP -> {
                drawings.map {
                    if (it is ConcatenationFunction) {
                        when {
                            it.xAxis is TopAxis -> {
                                it.xAxis
                            }
                            it.yAxis is TopAxis -> {
                                it.yAxis
                            }
                        }
                    }
                }.count()
            }
            Direction.BOTTOM -> drawings.map {
                if (it is ConcatenationFunction) {
                    when {
                        it.xAxis is BottomAxis -> {
                            it.xAxis
                        }
                        it.yAxis is BottomAxis -> {
                            it.yAxis
                        }
                    }
                }
            }.count()
        } * AbstractAxis.WIDTH_AXIS
    }
}