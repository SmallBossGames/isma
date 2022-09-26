plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")
}

group = "ru.nstu.isma"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(libs.kotlinx.serialization.json)
}

val moduleName by extra("isma.isma.next.services.simulation.abstractions.main")

tasks {
    compileJava {
        inputs.property("moduleName", moduleName)
        options.compilerArgs = listOf(
            "--patch-module", "$moduleName=${sourceSets.main.get().output.asPath}"
        )
    }
}