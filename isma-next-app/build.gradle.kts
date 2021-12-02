plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")

    id("org.openjfx.javafxplugin")

    application
}

javafx {
    version = "17"
    modules = listOf("javafx.controls", "javafx.fxml")
}

application {
    mainClass.set("ru.isma.next.app.launcher.LauncherKt")
}

val koinVersion = "3.1.3"
val kotlinReflectVersion = "1.6.0"
val kotlinxCoroutinesVersion = "1.6.0-RC"
val kotlinxSerializationJsonVersion = "1.3.1"

dependencies {
    implementation ("no.tornado:tornadofx:1.7.20")
    implementation ("org.antlr:antlr4-runtime:4.9.3")
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-core:$kotlinxCoroutinesVersion")
    implementation ("org.jetbrains.kotlin:kotlin-reflect:$kotlinReflectVersion")
    implementation ("io.insert-koin:koin-core:$koinVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$kotlinxSerializationJsonVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-javafx:1.6.0-RC")
    implementation("org.kordamp.ikonli:ikonli-javafx:12.2.0")
    implementation("org.kordamp.ikonli:ikonli-material2-pack:12.2.0")
    implementation ("org.fxmisc.richtext:richtextfx:0.10.7")
    implementation("org.controlsfx:controlsfx:11.1.0")

    implementation(project(":isma-hsm"))
    implementation(project(":isma-lisma"))
    implementation(project(":isma-next-core"))
    implementation(project(":isma-intg-api"))
    implementation(project(":isma-next-integration-library"))
    implementation(project(":grin:integration"))
    implementation(project(":isma-next-common-services"))
    implementation(project(":isma-blueprint-editor"))
    implementation(project(":isma-text-editor"))
    implementation(project(":isma-next-services-simulation-abstractions"))

    api(project(":isma-intg-core"))
    api(project(":isma-intg-server:isma-intg-server-client"))
}