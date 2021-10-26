plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")

    id("org.openjfx.javafxplugin")
}

group = "ru.nstu.isma"
version = "1.0.0"

javafx {
    version = "17"
    modules = listOf("javafx.controls", "javafx.fxml")
}

val kotlinxSerializationJsonVersion = "1.3.0"

dependencies {
    implementation(kotlin("stdlib"))

    implementation(project(":isma-text-editor"))

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$kotlinxSerializationJsonVersion")
    implementation ("org.fxmisc.richtext:richtextfx:0.10.7")
}
