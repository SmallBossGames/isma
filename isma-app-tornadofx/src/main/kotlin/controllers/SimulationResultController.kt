package controllers

import javafx.beans.property.SimpleObjectProperty
import javafx.stage.FileChooser
import ru.nstu.isma.intg.api.IntgResultPoint
import ru.nstu.isma.intg.api.calcmodel.DaeSystem
import ru.nstu.isma.next.core.sim.controller.HybridSystemIntgResult
import tornadofx.*
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.util.function.Consumer

class SimulationResultController : Controller() {
    private val commaAndSpace = ", "

    private val fileFilers = arrayOf(
            FileChooser.ExtensionFilter("Comma separate file", "*.csv")
    )

    val simulationResultProperty = SimpleObjectProperty<HybridSystemIntgResult>(null)
    var simulationResult by simulationResultProperty


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
        val deCount = equationIndexProvider.differentialEquationCount
        for (i in 0 until deCount) {
            header.append(equationIndexProvider.getDifferentialEquationCode(i)).append(commaAndSpace)
        }

        // Алгебраические переменные
        val aeCount: Int = equationIndexProvider.algebraicEquationCount
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