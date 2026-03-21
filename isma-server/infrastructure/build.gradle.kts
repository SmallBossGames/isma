plugins {
    alias(libs.plugins.kotlin.jvm)
}

group = "ru.nstu.isma.server"
version = "1.0.0-SNAPSHOT"

dependencies {
    implementation(project(":isma-server:domain"))
    implementation(project(":isma-solver:api"))

    implementation(libs.koin.core)
    implementation(libs.slf4j.api)
}
