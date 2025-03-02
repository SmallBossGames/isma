plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.java.modules)
}

group = "ru.nstu.isma"
version = "1.0.0"

dependencies {
    implementation(project(":isma-compiler:hsm-core"))
    implementation(project(":isma-compiler:lisma-hsm-translator"))
    implementation(project(":isma-next-core"))

    implementation (libs.tornadofx.core)
}