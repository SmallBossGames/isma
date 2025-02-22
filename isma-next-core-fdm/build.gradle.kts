plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.java.modules)
}

group = "ru.nstu.isma"
version = "1.0.0"

dependencies {
    implementation(project(":isma-hsm"))
}