package ru.isma.next.app.services.simualtion

import javafx.beans.binding.BooleanBinding
import javafx.beans.property.SimpleObjectProperty
import javafx.scene.paint.Color
import javafx.stage.FileChooser
import ru.nstu.grin.common.model.Point
import ru.nstu.grin.concatenation.axis.model.ConcatenationAxis
import ru.nstu.grin.concatenation.axis.model.Direction
import ru.nstu.grin.concatenation.cartesian.model.CartesianSpace
import ru.nstu.grin.concatenation.function.model.ConcatenationFunction
import ru.nstu.grin.concatenation.function.model.LineType
import ru.nstu.grin.integration.IntegrationController
import ru.nstu.isma.intg.api.calcmodel.DaeSystem
import ru.nstu.isma.next.core.sim.controller.HybridSystemIntgResult
import tornadofx.*
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.util.UUID

class SimulationResultService(private val grinIntegrationController: IntegrationController) {
    private val fileFilers = arrayOf(
            FileChooser.ExtensionFilter("Comma separate file", "*.csv")
    )

    private val simulationResultProperty = SimpleObjectProperty<HybridSystemIntgResult>(null)
    fun simulationResultProperty() = simulationResultProperty
    var simulationResult: HybridSystemIntgResult? by simulationResultProperty

    private val isResultAvailableProperty = simulationResultProperty().isNotNull
    fun isResultAvailableProperty(): BooleanBinding = isResultAvailableProperty
    val isResultAvailable by isResultAvailableProperty

    fun showChart() {
        simulationResult?:return;

        val xAxis = ConcatenationAxis(
            id = UUID.randomUUID(),
            name = "tt",
            order = 0,
            direction = Direction.LEFT,
            backGroundColor = Color.BLACK,
            fontColor = Color.CYAN,
            distanceBetweenMarks = 40.0,
            textSize = 12.0,
            font = "Arial"
        )

        val yAxis = ConcatenationAxis(
            id = UUID.randomUUID(),
            name = "tt",
            order = 0,
            direction = Direction.BOTTOM,
            backGroundColor = Color.BLACK,
            fontColor = Color.CYAN,
            distanceBetweenMarks = 40.0,
            textSize = 12.0,
            font = "Arial"
        )

        val headers = createColumnNamesArray(simulationResult!!)
        val columns = createResultColumns(simulationResult!!, headers.size)

        val functions = arrayListOf<ConcatenationFunction>()

        for (i in headers.indices) {
            functions.add(createSimpleConcatenationFunction(headers[i], columns[i].toList()))
        }

        val cartesianSpace = CartesianSpace(
            UUID.randomUUID(),
            "SomeSpace",
            functions,
            xAxis,
            yAxis)

        val spaces = listOf(cartesianSpace)
        grinIntegrationController.integrate(spaces)
    }

    fun exportToFile(){
        val selectedFiles = chooseFile (filters = fileFilers, mode = FileChooserMode.Save)

        if(selectedFiles.isEmpty())
        {
            return
        }

        val file = selectedFiles.first()

        exportToFile(file)
    }

    fun exportToFile(file: File){
        simulationResult ?: return
        try {
            FileWriter(file).buffered().use { writer ->
                val header = buildHeader(simulationResult!!)
                writer.write(header)
                val points = buildPoints(simulationResult!!)
                writer.write(points)
            }
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
    }

    fun clean() {
        simulationResult = null
    }

    private fun buildHeader(result: HybridSystemIntgResult): String {
        val header = StringBuilder()

        // x
        header.append("x").append(COMMA_AND_SPACE)
        val equationIndexProvider = result.equationIndexProvider!!

        // Дифференциальные переменные
        val deCount = equationIndexProvider.getDifferentialEquationCount()
        for (i in 0 until deCount) {
            header.append(equationIndexProvider.getDifferentialEquationCode(i)).append(COMMA_AND_SPACE)
        }

        // Алгебраические переменные
        val aeCount: Int = equationIndexProvider.getAlgebraicEquationCount()
        for (i in 0 until aeCount) {
            header.append(equationIndexProvider.getAlgebraicEquationCode(i)).append(COMMA_AND_SPACE)
        }

        // Правая часть
        for (i in 0 until deCount) {
            header.append("f").append(i.toString()).append(COMMA_AND_SPACE)
        }

        // Удаляем последний пробел и запятую и заменяем на перенос строки
        header.delete(header.length - 2, header.length).appendLine()
        return header.toString()
    }

    private fun buildPoints(result: HybridSystemIntgResult): String {
        val points = StringBuilder()
        if (result.resultPointProvider != null) {
            result.resultPointProvider!!.read {
                for (p in it) {
                    // x
                    points.append(p.x).append(COMMA_AND_SPACE)

                    // Дифференциальные переменные
                    for (yForDe in p.yForDe) {
                        points.append(yForDe).append(COMMA_AND_SPACE)
                    }

                    // Алгебраические переменные
                    for (yForAe in p.rhs[DaeSystem.RHS_AE_PART_IDX]) {
                        points.append(yForAe).append(COMMA_AND_SPACE)
                    }

                    // Правая часть
                    for (f in p.rhs[DaeSystem.RHS_DE_PART_IDX]) {
                        points.append(f).append(COMMA_AND_SPACE)
                    }

                    // Удаляем последний пробел и запятую и заменяем на перенос строки
                    points.delete(points.length - 2, points.length).appendLine()
                }
            }
        }
        return points.toString()
    }

    private fun createColumnNamesArray(result: HybridSystemIntgResult) : Array<String> {
        val equationIndexProvider = result.equationIndexProvider!!
        val deCount = equationIndexProvider.getDifferentialEquationCount()
        val aeCount: Int = equationIndexProvider.getAlgebraicEquationCount()
        val outputArray = Array(deCount*2 + aeCount) { "" }

        for (i in 0 until deCount) {
            outputArray[i] = equationIndexProvider.getDifferentialEquationCode(i) ?: ""
        }

        var offset = deCount

        for (i in 0 until aeCount) {
            outputArray[i + offset] = equationIndexProvider.getAlgebraicEquationCode(i) ?: ""
        }

        offset = aeCount + deCount

        for (i in 0 until deCount) {
            outputArray[i + offset] = "f${i}"
        }

        return outputArray
    }

    private fun createResultColumns(result: HybridSystemIntgResult, columnsCount: Int) : Array<Array<Point>> {
        val pointsProvider = result.resultPointProvider!!
        var outputArray = emptyArray<Array<Point>>()
        pointsProvider.read {
            val rowsCount = it.size
            val tempArray = Array(columnsCount) { Array(rowsCount) { Point.Zero } }
            for (i in it.indices) {
                val x = it[i].x

                for (j in it[i].yForDe.indices) {
                    tempArray[j][i] = Point(x, it[i].yForDe[j])
                }

                var offset = it[i].yForDe.size

                for (j in it[i].rhs[DaeSystem.RHS_AE_PART_IDX].indices) {
                    tempArray[j + offset][i] = Point(x, it[i].rhs[DaeSystem.RHS_AE_PART_IDX][j])
                }

                offset = it[i].yForDe.size + it[i].rhs[DaeSystem.RHS_AE_PART_IDX].size

                for (j in it[i].rhs[DaeSystem.RHS_DE_PART_IDX].indices) {
                    tempArray[j + offset][i] = Point(x, it[i].rhs[DaeSystem.RHS_DE_PART_IDX][j])
                }
            }
            outputArray = tempArray
        }
        return outputArray
    }

    private fun createSimpleConcatenationFunction(name: String, points: List<Point>) : ConcatenationFunction {
        return ConcatenationFunction(
            id = UUID.randomUUID(),
            name = name,
            points = points,
            isHide = false,
            isSelected = false,
            functionColor = Color.color(Math.random(), Math.random(), Math.random()),
            lineSize = 1.0,
            lineType = LineType.POLYNOM
        )
    }

    companion object {
        private const val COMMA_AND_SPACE = ", "
    }
}