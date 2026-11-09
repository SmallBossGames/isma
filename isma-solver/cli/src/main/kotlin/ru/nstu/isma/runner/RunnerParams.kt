package ru.nstu.isma.runner

/**
 * Parsed command-line parameters for the simulation runner CLI.
 *
 * The CLI is invoked with `--name value` pairs and boolean flags. Required
 * options: `--model`, `--start`, `--end`, `--step`, `--method`. Optional:
 * `--accuracy`, `--accuracy-enabled`, `--stability-enabled`, `--gamma`,
 * `--low-border`.
 */
data class RunnerParams(
    val model: String,
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
        private val VALUE_OPTIONS = setOf(
            "model", "start", "end", "step", "method", "accuracy", "gamma", "low-border",
        )
        private val FLAG_OPTIONS = setOf("accuracy-enabled", "stability-enabled")
        private val REQUIRED = setOf("model", "start", "end", "step", "method")

        /**
         * Parses [args] into a [RunnerParams].
         *
         * @throws IllegalArgumentException on an unknown option, a missing value,
         *   a missing required option, or a non-positive `--step`.
         */
        fun parse(args: Array<String>): RunnerParams {
            val values = LinkedHashMap<String, String>()
            val flags = LinkedHashSet<String>()

            var i = 0
            while (i < args.size) {
                val arg = args[i]
                require(arg.startsWith("--")) { "Unexpected argument: $arg" }
                val name = arg.removePrefix("--")
                require(name in VALUE_OPTIONS || name in FLAG_OPTIONS) { "Unknown option: $arg" }
                if (name in FLAG_OPTIONS) {
                    flags.add(name)
                } else {
                    require(i + 1 < args.size) { "Missing value for $arg" }
                    values[name] = args[i + 1]
                    i++
                }
                i++
            }

            REQUIRED.forEach { name ->
                require(name in values) { "Missing required option --$name" }
            }
            require(values.getValue("step").toDouble() > 0.0) { "Option --step must be positive" }

            return RunnerParams(
                model = values.getValue("model"),
                start = values.getValue("start").toDouble(),
                end = values.getValue("end").toDouble(),
                step = values.getValue("step").toDouble(),
                method = values.getValue("method"),
                accuracy = values["accuracy"]?.toDouble() ?: 0.0,
                accuracyEnabled = "accuracy-enabled" in flags,
                stabilityEnabled = "stability-enabled" in flags,
                gamma = values["gamma"]?.toDouble(),
                lowBorder = values["low-border"]?.toDouble(),
            )
        }
    }
}
