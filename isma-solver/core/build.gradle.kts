plugins {
    alias(libs.plugins.java.modules)
    alias(libs.plugins.kotlin.jvm)
}

dependencies {
    implementation(project(":isma-solver:api"))
    implementation(project(":isma-compiler:hsm-jvm-calcmodel"))

    implementation(libs.jetbrains.annotations)
    implementation(libs.kotlinx.coroutines.core)
}