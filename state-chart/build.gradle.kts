import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.4.30"
    id("org.openjfx.javafxplugin") version "0.0.9"
}

javafx {
    version = "11.0.2"
    modules("javafx.controls", "javafx.graphics")
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.4.30")
    implementation("no.tornado:tornadofx:1.7.20")
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "11"
}