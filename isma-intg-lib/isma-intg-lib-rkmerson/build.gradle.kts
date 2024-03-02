plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.java.modules)
}

dependencies {
    implementation(project(":isma-intg-api"))
    implementation(project(":isma-intg-core"))
}
