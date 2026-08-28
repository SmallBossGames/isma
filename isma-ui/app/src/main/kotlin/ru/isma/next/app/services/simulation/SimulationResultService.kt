package ru.isma.next.app.services.simulation

import kotlinx.coroutines.*
import ru.isma.javafx.extensions.coroutines.UiThreadExecutor
import ru.isma.next.app.launcher.GrinProcessLauncher
import ru.isma.next.app.models.simulation.CompletedSimulationModel
import ru.isma.next.app.models.simulation.SimulationTask
import ru.isma.next.domain.models.SimulationPoint
import ru.isma.next.external.BinaryFilePointProvider
import java.io.File
import java.io.Writer

class SimulationResultService(
    private val grinProcessLauncher: GrinProcessLauncher,
    private val simulationTaskService: ISimulationTaskService,
    private val uiThreadExecutor: UiThreadExecutor,
) {

    private val resultServiceScope = CoroutineScope(Dispatchers.Default + SupervisorJob())

    fun removeResult(task: SimulationTask) = uiThreadExecutor.executeOnUi {
        simulationTaskService.removeTask(task)
        task.result = null
    }

    fun launchChart(result: CompletedSimulationModel, xAxisName: String, columnNames: List<String>) {
        resultServiceScope.launch {
            grinProcessLauncher.launch(result.cachedFile, xAxisName, columnNames)
        }
    }

    fun exportToFile(result: CompletedSimulationModel, file: File) {
        resultServiceScope.launch {
            exportToFileAsync(result, file)
        }
    }

    suspend fun exportToFileAsync(simulationResult: CompletedSimulationModel, file: File) = withContext(Dispatchers.IO) {
        file.bufferedWriter().use { writer ->
            val header = buildHeader(simulationResult)
            writer.write(header)
            writePoints(simulationResult, writer)
        }
    }

    private suspend fun writePoints(result: CompletedSimulationModel, writer: Writer) = coroutineScope {
        val pointProvider = BinaryFilePointProvider(result.cachedFile, result.cachedColumnNames)

        pointProvider.results.collect { value ->
            writer.appendLine(value.toCsvLine())
        }
    }

    private fun SimulationPoint.toCsvLine() : String {
        val builder = StringBuilder()

        builder.append(x).append(COMMA_AND_SPACE)

        for (yForDe in yForDe) {
            builder.append(yForDe).append(COMMA_AND_SPACE)
        }

        for (yForAe in rhs[RHS_AE_PART_IDX]) {
            builder.append(yForAe).append(COMMA_AND_SPACE)
        }

        for (f in rhs[RHS_DE_PART_IDX]) {
            builder.append(f).append(COMMA_AND_SPACE)
        }

        builder.delete(builder.length - 2, builder.length)

        return builder.toString()
    }

    fun close() {
        resultServiceScope.cancel()
    }

    companion object {
        private const val COMMA_AND_SPACE = ", "
        private const val RHS_DE_PART_IDX = 0
        private const val RHS_AE_PART_IDX = 1

        private fun buildHeader(result: CompletedSimulationModel): String {
            val header = StringBuilder()

            header.append("x").append(COMMA_AND_SPACE)
            val equationIndexProvider = result.equationIndexProvider

            val deCount = equationIndexProvider.getDifferentialEquationCount()
            for (i in 0 until deCount) {
                header.append(equationIndexProvider.getDifferentialEquationCode(i)).append(COMMA_AND_SPACE)
            }

            val aeCount: Int = equationIndexProvider.getAlgebraicEquationCount()
            for (i in 0 until aeCount) {
                header.append(equationIndexProvider.getAlgebraicEquationCode(i)).append(COMMA_AND_SPACE)
            }

            for (i in 0 until deCount) {
                header.append("f").append(i.toString()).append(COMMA_AND_SPACE)
            }

            header.delete(header.length - 2, header.length).appendLine()
            return header.toString()
        }
    }
}
