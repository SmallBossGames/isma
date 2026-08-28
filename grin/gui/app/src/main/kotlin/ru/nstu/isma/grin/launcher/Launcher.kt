package ru.nstu.isma.grin.launcher

import javafx.application.Application

fun main(args: Array<String>) {
    Application.launch(GrinApplication::class.java, *args)
}

data class GrinCommandLineConfig(
    val resultFile: String?,
    val xAxis: String?,
    val charts: List<String>,
) {
    companion object {
        fun parse(args: Array<String>): GrinCommandLineConfig {
            val resultFileIdx = args.indexOf("--result-file").takeIf { it >= 0 }?.let { it + 1 }
                ?: args.indexOf("-r").takeIf { it >= 0 }?.let { it + 1 }
                ?: args.indexOfFirst { !it.startsWith("-") }.takeIf { it >= 0 }

            val resultFile = resultFileIdx?.let { args[it] }

            val xAxisIdx = args.indexOf("--x-axis").takeIf { it >= 0 }?.let { it + 1 }
                ?: args.indexOf("-x").takeIf { it >= 0 }?.let { it + 1 }

            val xAxis = xAxisIdx?.let { args[it] }

            val chartsIdx = args.indexOf("--charts").takeIf { it >= 0 }?.let { it + 1 }
                ?: args.indexOf("-c").takeIf { it >= 0 }?.let { it + 1 }

            val charts = chartsIdx?.let { idx ->
                args.getOrNull(idx)?.split(",")?.map { it.trim() }?.filter { it.isNotEmpty() }
            } ?: emptyList()

            if (resultFile != null) {
                if (xAxis == null || charts.isEmpty()) {
                    throw IllegalArgumentException("When --result-file is provided, both --x-axis and --charts are required")
                }
            }

            return GrinCommandLineConfig(resultFile, xAxis, charts)
        }
    }
}