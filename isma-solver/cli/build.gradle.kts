plugins {
    alias(libs.plugins.kotlin.jvm)
    application
}

group = "ru.nstu.isma"
version = "1.0.0"

application {
    mainClass.set("ru.nstu.isma.runner.SimulationRunnerCliKt")
    applicationName = "simulation-runner-cli"
}

// Distinct jar name: this module is named `cli` (like lisma-compiler:cli), so the
// default `cli-<version>.jar` would collide in the daemon's lib. Name it after the
// application so the batteries-included runner jar is unambiguous.
tasks.jar {
    archiveBaseName.set("simulation-runner-cli")
}

dependencies {
    implementation(project(":lisma-compiler:runtime"))
    implementation(project(":isma-solver:api"))
    implementation(project(":isma-solver:core"))
    implementation(project(":isma-solver:lib-utils"))
    // lib-meta transitively pulls in every integration-method lib (euler, rk2, ...)
    // so the ServiceLoader discovers all factories at runtime.
    implementation(project(":isma-solver:lib-meta"))

    implementation(libs.slf4j.api)
}
