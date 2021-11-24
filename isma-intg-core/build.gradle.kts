plugins {
    kotlin("jvm")

    id("ch.tutteli.gradle.plugins.kotlin.module.info")
}

dependencies {
    implementation(project(":isma-intg-api"))
    implementation("org.slf4j:slf4j-api:1.7.32")
    implementation("org.apache.commons:commons-lang3:3.12.0")
    implementation("org.jetbrains:annotations:22.0.0")
}
