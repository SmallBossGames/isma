plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.java.modules)
}

group = "ru.nstu.isma.next"
version = "1.0.0"

dependencies {
    implementation(project(":isma-solver:api"))

    implementation(libs.slf4j.api)
}
