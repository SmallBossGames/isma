package ru.nstu.grin.simple.controller

import jwave.Transform
import jwave.transforms.AncientEgyptianDecomposition
import jwave.transforms.FastWaveletTransform
import jwave.transforms.wavelets.Wavelet
import jwave.transforms.wavelets.biorthogonal.BiOrthogonal68
import jwave.transforms.wavelets.coiflet.Coiflet5
import jwave.transforms.wavelets.haar.Haar1
import jwave.transforms.wavelets.legendre.Legendre3
import jwave.transforms.wavelets.other.DiscreteMayer
import ru.nstu.grin.common.model.Point
import ru.nstu.grin.common.model.WaveletDirection
import ru.nstu.grin.simple.events.FileCheckedEvent
import ru.nstu.grin.simple.model.PointsViewModel
import ru.nstu.grin.common.model.WaveletTransformFun
import tornadofx.Controller
import java.io.File
import java.io.FileInputStream

class PointsViewController : Controller() {
    private val model: PointsViewModel by inject(params = params)

    fun readPoints() {
        model.pointsProperty.clear()
        model.pointsProperty.addAll(readPoints(model.file))
    }

    private fun readPoints(file: File): List<Point> = FileInputStream(file).use {
        val lines = String(it.readBytes()).split("\n")
        lines.map { line ->
            val coordinates = line.split(model.delimiter)
            Point(coordinates[0].toDouble(), coordinates[1].toDouble())
        }
    }

    fun fireCheckedEvent() {
        val points = if (model.isWavelet) {
            val direction = model.waveletDirection
            if (direction == null) {
                tornadofx.error("Необходимо выбрать направление wavelet преобразования")
                return
            }
            transformWaveletPoints(direction)
        } else {
            model.points
        }
        val event = FileCheckedEvent(
            points = points
        )
        fire(event)
    }

    private fun transformWaveletPoints(direction: WaveletDirection): List<Point> {
        val transform = Transform(
            AncientEgyptianDecomposition(
                FastWaveletTransform(getWavelet())
            )
        )
        val xArray = model.points.map { it.x }.toDoubleArray()
        val yArray = model.points.map { it.y }.toDoubleArray()

        return when (direction) {
            WaveletDirection.X -> {
                val xTransformed = transform.forward(xArray).sorted()

                xTransformed.mapIndexed { index, d ->
                    Point(d, yArray[index])
                }
            }
            WaveletDirection.Y -> {
                val yTransformed = transform.forward(yArray).sorted()

                xArray.mapIndexed { index, d ->
                    Point(d, yTransformed[index])
                }
            }
            WaveletDirection.BOTH -> {
                val xTransformed = transform.forward(xArray).sorted()
                val yTransformed = transform.forward(yArray).sorted()

                xTransformed.mapIndexed { index, d ->
                    Point(d, yTransformed[index])
                }
            }
        }
    }

    private fun getWavelet(): Wavelet {
        return when (model.waveletTransformFun) {
            WaveletTransformFun.HAAR1 -> Haar1()
            WaveletTransformFun.COIFLET5 -> Coiflet5()
            WaveletTransformFun.BIORTHOGONAL68 -> BiOrthogonal68()
            WaveletTransformFun.DISCRETE_MAYER -> DiscreteMayer()
            WaveletTransformFun.LEGENDRE3 -> Legendre3()
            else -> {
                throw IllegalArgumentException("Something went wrong")
            }
        }
    }
}