package ru.nstu.isma.grin.launcher

import javafx.application.Application

fun main(args: Array<String>) {
    Application.launch(GrinApplication::class.java, *args)
}

data class GrinCommandLineConfig(
    val resultFile: String,
    val charts: List<String>,
) {
    companion object {
        fun parse(args: Array<String>): GrinCommandLineConfig {
            val resultFileIdx = args.indexOf("--result-file").takeIf { it >= 0 }?.let { it + 1 }
                ?: args.indexOf("-r").takeIf { it >= 0 }?.let { it + 1 }
                ?: args.indexOfFirst { !it.startsWith("-") }.takeIf { it >= 0 }

            val resultFile = resultFileIdx?.let { args[it] }
                ?: throw IllegalArgumentException("Missing required argument: --result-file <path>")

            val chartsIdx = args.indexOf("--charts").takeIf { it >= 0 }?.let { it + 1 }
                ?: args.indexOf("-c").takeIf { it >= 0 }?.let { it + 1 }

            val charts = chartsIdx?.let { idx ->
                args.getOrNull(idx)?.split(",")?.map { it.trim() }?.filter { it.isNotEmpty() }
            } ?: emptyList()

            return GrinCommandLineConfig(resultFile, charts)
        }
    }
}