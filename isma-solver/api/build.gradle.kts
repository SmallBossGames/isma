plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.java.modules)
}

dependencies{
    implementation(project(":isma-compiler:hsm-jvm-calcmodel"))

    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.jetbrains.annotations)
}
