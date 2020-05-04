package ru.nstu.grin.concatenation.points.controller

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
import ru.nstu.grin.concatenation.points.events.FileCheckedEvent
import ru.nstu.grin.concatenation.file.options.model.FileReaderMode
import ru.nstu.grin.concatenation.points.model.PointsViewModel
import ru.nstu.grin.common.model.WaveletDirection
import ru.nstu.grin.concatenation.file.options.model.CsvDetails
import ru.nstu.grin.concatenation.file.options.model.ExcelDetails
import ru.nstu.grin.concatenation.file.readers.*
import ru.nstu.grin.concatenation.function.model.FileType
import tornadofx.Controller
import java.io.File
import java.io.FileInputStream

class PointsViewController : Controller() {
    private val model: PointsViewModel by inject(params = params)

    fun readPoints() {
        model.pointsListProperty.clear()
        model.pointsListProperty.addAll(readPoints(model.file))
    }

    private fun readPoints(file: File): List<List<String>> {
        val fileType = FileRecognizer.recognize(file)
        return when (val details = model.details) {
            is ExcelDetails -> {
                if (fileType == FileType.XLSX) {
                    return XLSXReader().read(file, details.sheetName, ExcelRange(details.range))
                }
                if (fileType == FileType.XLS) {
                    return XLSReader().read(file, details.sheetName, ExcelRange(details.range))
                }
                throw IllegalArgumentException("Something went wrong can't match details and fileType")
            }
            is CsvDetails -> {
                CsvReader().read(file, details.delimiter, model.readerMode)
            }
        }
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
            val direction = model.waveletDirection
            if (direction == null) {
                tornadofx.error("Необходимо выбрать направление преобразования")
                return
            }
            makeWaveletTransform(points, direction)
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

    private fun makeWaveletTransform(
        points: List<List<Point>>,
        direction: WaveletDirection
    ): List<List<Point>> {
        val transform = Transform(
            AncientEgyptianDecomposition(
                FastWaveletTransform(getWavelet())
            )
        )
        return points.map { functionPoints ->
            val xArray = functionPoints.map { it.x }.toDoubleArray()
            val yArray = functionPoints.map { it.y }.toDoubleArray()

            when (direction) {
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