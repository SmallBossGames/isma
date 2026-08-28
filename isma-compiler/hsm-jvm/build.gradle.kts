plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.java.modules)
}

group = "ru.nstu.isma"
version = "1.0.0"

dependencies {
    implementation(libs.com.google.guava)
    implementation(libs.slf4j.api)

    implementation(project(":isma-compiler:hsm-core"))
    implementation(project(":isma-compiler:hsm-jvm-calcmodel"))
}