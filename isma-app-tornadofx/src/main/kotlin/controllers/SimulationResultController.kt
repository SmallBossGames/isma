package controllers

import javafx.beans.property.SimpleObjectProperty
import javafx.scene.paint.Color
import javafx.stage.FileChooser
import ru.nstu.grin.concatenation.axis.model.ConcatenationAxis
import ru.nstu.grin.concatenation.axis.model.Direction
import ru.nstu.grin.concatenation.cartesian.model.CartesianSpace
import ru.nstu.grin.concatenation.function.model.ConcatenationFunction
import ru.nstu.grin.concatenation.function.model.LineType
import ru.nstu.grin.integration.IntegrationController
import ru.nstu.isma.intg.api.IntgResultPoint
import ru.nstu.isma.intg.api.calcmodel.DaeSystem
import ru.nstu.isma.next.core.sim.controller.HybridSystemIntgResult
import tornadofx.*
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.util.*
import java.util.function.Consumer

class SimulationResultController : Controller() {
    private val commaAndSpace = ", "

    private val fileFilers = arrayOf(
            FileChooser.ExtensionFilter("Comma separate file", "*.csv")
    )

    private val grinIntegrationController by inject<IntegrationController>()

    val simulationResultProperty = SimpleObjectProperty<HybridSystemIntgResult>(null)
    var simulationResult by simulationResultProperty


    fun showChart() {
        val xAxis = ConcatenationAxis(
            UUID.randomUUID(),
            "xAxis",
            0,
            Direction.RIGHT,
            Color.BLUE,
            Color.BLACK,
            40.0,
            12.0,
            "Arial"
        )

        val yAxis = ConcatenationAxis(
            UUID.randomUUID(),
            "yAxis",
            0,
            Direction.TOP,
            Color.BLUE,
            Color.BLACK,
            40.0,
            12.0,
            "Arial")

        val conctainationFunction = ConcatenationFunction(
            UUID.randomUUID(),
            "SomeName",
            listOf(),
            false,
            false,
            Color.BLUE,
            0.1,
            LineType.CIRCLE_FILL_DOTES,
        )

        val cartesianSpace = CartesianSpace(UUID.randomUUID(), "SomeSpace", mutableListOf(conctainationFunction), xAxis, yAxis)
        val spaces = listOf(cartesianSpace)
        grinIntegrationController.integrate(spaces)
    }

    fun exportToFile(){
        val selectedFiles = chooseFile (filters = fileFilers, mode = FileChooserMode.Save)

        if(selectedFiles.isEmpty())
        {
            return;
        }

        val file = selectedFiles.first()

        exportToFile(file)
    }

    fun exportToFile(file: File){
        simulationResult ?: return
        try {
            FileWriter(file).buffered().use { writer ->
                val header = buildHeader(simulationResult)
                writer.write(header)
                val points = buildPoints(simulationResult)
                writer.write(points)
            }
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
    }

    private fun buildHeader(result: HybridSystemIntgResult): String {
        val header = StringBuilder()

        // x
        header.append("x").append(commaAndSpace)
        val equationIndexProvider = result.equationIndexProvider!!

        // Дифференциальные переменные
        val deCount = equationIndexProvider.getDifferentialEquationCount()
        for (i in 0 until deCount) {
            header.append(equationIndexProvider.getDifferentialEquationCode(i)).append(commaAndSpace)
        }

        // Алгебраические переменные
        val aeCount: Int = equationIndexProvider.getAlgebraicEquationCount()
        for (i in 0 until aeCount) {
            header.append(equationIndexProvider.getAlgebraicEquationCode(i)).append(commaAndSpace)
        }

        // Правая часть
        for (i in 0 until deCount) {
            header.append("f").append(i.toString()).append(commaAndSpace)
        }

        // Удаляем последний пробел и запятую и заменяем на перенос строки
        header.delete(header.length - 2, header.length).appendLine()
        return header.toString()
    }

    private fun buildPoints(result: HybridSystemIntgResult): String {
        val points = java.lang.StringBuilder()
        if (result.resultPointProvider != null) {
            result.resultPointProvider!!.read(Consumer { intgResultPoints: List<IntgResultPoint> ->
                for (p in intgResultPoints) {
                    // x
                    points.append(p.x).append(commaAndSpace)

                    // Дифференциальные переменные
                    for (yForDe in p.yForDe) {
                        points.append(yForDe).append(commaAndSpace)
                    }

                    // Алгебраические переменные
                    for (yForAe in p.rhs[DaeSystem.RHS_AE_PART_IDX]) {
                        points.append(yForAe).append(commaAndSpace)
                    }

                    // Правая часть
                    for (f in p.rhs[DaeSystem.RHS_DE_PART_IDX]) {
                        points.append(f).append(commaAndSpace)
                    }

                    // Удаляем последний пробел и запятую и заменяем на перенос строки
                    points.delete(points.length - 2, points.length).appendLine()
                }
            })
        }
        return points.toString()
    }
}