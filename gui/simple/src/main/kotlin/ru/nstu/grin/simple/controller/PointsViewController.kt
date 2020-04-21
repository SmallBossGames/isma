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
import ru.nstu.grin.simple.events.FileCheckedEvent
import ru.nstu.grin.simple.model.PointsViewModel
import ru.nstu.grin.simple.model.WaveletTransformFun
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
        lines.mapNotNull { line ->
            when {
                line.contains(",") -> {
                    val coordinates = line.split(",")
                    Point(coordinates[0].toDouble(), coordinates[1].toDouble())
                }
                line.contains(";") -> {
                    val coordinates = line.split(";")
                    Point(coordinates[0].toDouble(), coordinates[1].toDouble())
                }
                else -> null
            }
        }
    }

    fun fireCheckedEvent() {
        val points = if (model.isWavelet) {
            val transform = Transform(
                AncientEgyptianDecomposition(
                    FastWaveletTransform(getWavelet())
                )
            )
            val xArray = model.points.map { it.x }.toDoubleArray()
            val yArray = model.points.map { it.y }.toDoubleArray()

            val xTransformed = transform.forward(xArray)
            val yTransformed = transform.forward(yArray)

            xTransformed.mapIndexed { index, d ->
                Point(d, yTransformed[index])
            }
        } else {
            model.points
        }
        val event = FileCheckedEvent(
            points = points
        )
        fire(event)
    }

    private fun getWavelet(): Wavelet {
        return when (model.waveletTransformFun) {
            WaveletTransformFun.HAAR1 -> Haar1()
            WaveletTransformFun.COIFLET5 -> Coiflet5()
            WaveletTransformFun.BIORTHOGONAL68 -> BiOrthogonal68()
            WaveletTransformFun.DISCRETE_MAYER -> DiscreteMayer()
            WaveletTransformFun.LEGENDRE3 -> Legendre3()
            else -> {
                throw IllegalArgumentException("Somethind went wrong")
            }
        }
    }
}