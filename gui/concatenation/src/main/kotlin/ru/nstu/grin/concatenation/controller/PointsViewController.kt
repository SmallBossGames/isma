package ru.nstu.grin.concatenation.controller

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
import ru.nstu.grin.common.model.WaveletTransformFun
import ru.nstu.grin.concatenation.events.FileCheckedEvent
import ru.nstu.grin.concatenation.model.FileReaderMode
import ru.nstu.grin.concatenation.model.PointsViewModel
import tornadofx.Controller
import java.io.File
import java.io.FileInputStream

class PointsViewController : Controller() {
    private val model: PointsViewModel by inject(params = params)

    fun readPoints() {
        model.pointsListProperty.clear()
        model.pointsListProperty.addAll(readPoints(model.file))
    }

    private fun readPoints(file: File): List<List<String>> = FileInputStream(file).use {
        val lines = String(it.readBytes()).split("\n")
        val result = mutableListOf<List<String>>()
        for (line in lines) {
            val coordinates = line.split(model.delimiter)
            if (model.readerMode == FileReaderMode.SEQUENCE && coordinates.size % 2 != 0) {
                tornadofx.error("Неверное количество колонок")
                return@use emptyList()
            }
            result.add(coordinates)
        }
        result
    }

    private fun <T> List<List<T>>.transpose(): List<List<T>> {
        if (this.isEmpty()) return this

        val result = mutableListOf<List<T>>()
        for (i in this[0].indices) {
            val column = mutableListOf<T>()
            for (row in this) {
                if (row.size <= i) continue
                column.add(row[i])
            }
            result.add(column)
        }
        return result
    }

    fun sendFireCheckedEvent() {
        val points = when (model.readerMode) {
            FileReaderMode.ONE_TO_MANY -> {
                model.pointsList.map { list ->
                    list.zipWithNext { a, b ->
                        Point(
                            list[0].replace(",", ".").toDouble(),
                            b.replace(",", ".").toDouble()
                        )
                    }
                }
            }
            FileReaderMode.SEQUENCE -> {
                model.pointsList.map {
                    it.zipWithNext { a, b ->
                        Point(
                            a.replace(",", ".").toDouble(),
                            b.replace(",", ".").toDouble()
                        )
                    }
                }
            }
        }.transpose()

        val transformed = if (model.isWavelet) {
            val transform = Transform(
                AncientEgyptianDecomposition(
                    FastWaveletTransform(getWavelet())
                )
            )
            points.map { functionPoints ->
                val xArray = functionPoints.map { it.x }.toDoubleArray()
                val yArray = functionPoints.map { it.y }.toDoubleArray()

                val xTransformed = transform.forward(xArray)
                val yTransformed = transform.forward(yArray)

                xTransformed.mapIndexed { index, d ->
                    Point(d, yTransformed[index])
                }
            }
        } else {
            points
        }


        fire(
            FileCheckedEvent(
                points = transformed,
                addFunctionsMode = model.addFunctionsMode
            )
        )
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