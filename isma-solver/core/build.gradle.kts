plugins {
    alias(libs.plugins.java.modules)
    alias(libs.plugins.kotlin.jvm)
}

dependencies {
    implementation(project(":isma-solver:api"))
    implementation(project(":isma-compiler:hsm-jvm-calcmodel"))

    implementation(libs.slf4j.api)
    implementation(libs.org.apache.commons.lang)
    implementation(libs.jetbrains.annotations)
}