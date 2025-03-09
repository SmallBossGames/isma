plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.java.modules)
}

dependencies {
    implementation(project(":isma-solver:api"))
    implementation(project(":isma-solver:core"))
}
