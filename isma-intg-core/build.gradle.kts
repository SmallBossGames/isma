plugins {
    alias(libs.plugins.java.modules)
    alias(libs.plugins.kotlin.jvm)
}

dependencies {
    implementation(project(":isma-intg-api"))
    implementation(libs.slf4j.api)
    implementation("org.apache.commons:commons-lang3:3.12.0")
    implementation(libs.jetbrains.annotations)
}