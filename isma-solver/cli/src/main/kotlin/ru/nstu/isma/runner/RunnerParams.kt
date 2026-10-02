package ru.nstu.isma.runner

/**
 * Parsed command-line parameters for the simulation runner CLI.
 */
data class RunnerParams(
    val model: String,
    val out: String,
    val progress: String?,
    val start: Double,
    val end: Double,
    val step: Double,
    val method: String,
    val accuracy: Double,
    val accuracyEnabled: Boolean,
    val stabilityEnabled: Boolean,
    val gamma: Double?,
    val lowBorder: Double?,
) {
    companion object {
        fun parse(args: Array<String>): RunnerParams {
            val values = HashMap<String, String>()
            val flags = HashSet<String>()
            var i = 0
            while (i < args.size) {
                val arg = args[i]
                when {
                    arg == "--accuracy-enabled" -> flags.add(arg)
                    arg == "--stability-enabled" -> flags.add(arg)
                    arg.startsWith("--") && i + 1 < args.size -> {
                        values[arg.removePrefix("--")] = args[i + 1]
                        i++
                    }
                    else -> error("Unexpected argument: $arg")
                }
                i++
            }

            require("model" in values) { "Missing required argument --model" }
            require("out" in values) { "Missing required argument --out" }
            require("start" in values) { "Missing required argument --start" }
            require("end" in values) { "Missing required argument --end" }
            require("step" in values) { "Missing required argument --step" }
            require("method" in values) { "Missing required argument --method" }

            return RunnerParams(
                model = values.getValue("model"),
                out = values.getValue("out"),
                progress = values["progress"],
                start = values.getValue("start").toDouble(),
                end = values.getValue("end").toDouble(),
                step = values.getValue("step").toDouble(),
                method = values.getValue("method"),
                accuracy = values["accuracy"]?.toDouble() ?: 0.0,
                accuracyEnabled = "--accuracy-enabled" in flags,
                stabilityEnabled = "--stability-enabled" in flags,
                gamma = values["gamma"]?.toDouble(),
                lowBorder = values["low-border"]?.toDouble(),
            )
        }
    }
}
