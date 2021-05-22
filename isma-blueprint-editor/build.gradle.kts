plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")

    id("org.openjfx.javafxplugin")
}

group = "ru.nstu.isma"
version = "1.0.0"

javafx {
    version = "16"
    modules = listOf("javafx.controls", "javafx.fxml")
}

repositories {
    mavenCentral()
}

val kotlinxSerializationJsonVersion = "1.2.1"

dependencies {
    implementation(kotlin("stdlib"))

    implementation ("no.tornado:tornadofx:1.7.20")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$kotlinxSerializationJsonVersion")
}
