package ru.nstu.grin.controller.function

import ru.nstu.grin.model.Point
import ru.nstu.grin.model.view.function.FileFunctionViewModel
import tornadofx.Controller
import java.io.BufferedInputStream
import java.io.File
import java.io.FileInputStream

/**
 * @author Konstantin Volivach
 */
class FileFunctionController : Controller() {
    private val model: FileFunctionViewModel by inject()

    private companion object {
        const val DELIMITER = ","
    }

    fun loadFunctions() {
        val file = readFile()
        val lines = file.split("\n")

        val listOfPoints = mutableListOf<List<Point>>()
        for (i in 0 until lines.size - 1) {
            val x = lines[i].split(DELIMITER)
            val y = lines[i + 1].split(DELIMITER)
            val zipped = x.zip(y) { a, b ->
                Point(a.toDouble(), b.toDouble())
            }
            listOfPoints.add(zipped)
        }

    }

    fun readFile(): String {
        val file = File(model.filePath)
        val inputStream = FileInputStream(file)
        val bufferedReader = BufferedInputStream(inputStream)
        val array = bufferedReader.readBytes()
        return String(array)
    }
}