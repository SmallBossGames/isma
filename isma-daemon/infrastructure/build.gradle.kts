plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.java.modules)
}

group = "ru.nstu.isma.daemon"
version = "1.0.0-SNAPSHOT"

dependencies {
    implementation(project(":isma-daemon:domain"))
    // The daemon is a pure orchestrator: it launches the compiler and runner
    // CLIs as separate processes. It holds no solver jars; ListSimulationMethods
    // asks the batteries-included runner for its method list.

    implementation(libs.koin.core)
    implementation(libs.slf4j.api)
}
