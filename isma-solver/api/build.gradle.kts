plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.java.modules)
}

dependencies{
    implementation(project(":isma-compiler:hsm-jvm-calcmodel"))

    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.jetbrains.annotations)
    implementation(libs.slf4j.api)
    implementation(libs.org.apache.commons.lang)
}
