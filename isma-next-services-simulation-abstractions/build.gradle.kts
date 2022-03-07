plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")
}

group = "ru.nstu.isma"
version = "1.0.0"

val kotlinxSerializationJsonVersion = "1.3.2"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$kotlinxSerializationJsonVersion")
}