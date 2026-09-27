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

dependencies {
    implementation(project(":isma-compiler:hsm-core"))
    implementation(project(":isma-compiler:hsm-jvm"))
    implementation(project(":isma-compiler:hsm-jvm-calcmodel"))
    implementation(project(":isma-next-core"))
    implementation(project(":isma-solver:api"))
    implementation(project(":isma-solver:core"))
    implementation(project(":isma-solver:lib-utils"))
    // lib-meta transitively pulls in every integration-method lib (euler, rk2, ...)
    // so the ServiceLoader discovers all factories at runtime.
    implementation(project(":isma-solver:lib-meta"))
    implementation(project(":isma-jvm-lib:exchange-format"))

    implementation(libs.slf4j.api)
}
