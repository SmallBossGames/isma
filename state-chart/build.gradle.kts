import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
    id("org.openjfx.javafxplugin") version "0.0.9"
}

javafx {
    version = "11.0.2"
    modules("javafx.controls", "javafx.graphics")
}

dependencies {
    implementation("no.tornado:tornadofx:1.7.19")
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "11"
}