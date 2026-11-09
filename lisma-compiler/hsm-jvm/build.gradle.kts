plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.java.modules)
}

group = "ru.nstu.isma"
version = "1.0.0"

dependencies {
    implementation(libs.com.google.guava)
    implementation(libs.slf4j.api)

    implementation(project(":lisma-compiler:hsm-core"))
    implementation(project(":lisma-compiler:hsm-jvm-calcmodel"))
}