import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")

    id("org.openjfx.javafxplugin")
    id("com.github.johnrengelman.shadow")

    application
}

javafx {
    version = "16"
    modules = listOf("javafx.controls", "javafx.fxml")
}

application {
    mainClass.set("ru.isma.next.app.launcher.LauncherKt")
    applicationDefaultJvmArgs = listOf(
        "--add-opens=javafx.controls/javafx.scene.control=ALL-UNNAMED",
        "--add-opens=javafx.graphics/javafx.scene=ALL-UNNAMED")
}

val koinVersion = "3.1.2"
val kotlinReflectVersion = "1.5.21"
val kotlinxCoroutinesVersion = "1.5.1-native-mt"
val kotlinxSerializationJsonVersion = "1.2.2"

dependencies {
    implementation ("no.tornado:tornadofx:1.7.20")
    implementation ("org.antlr:antlr4-runtime:4.9.2")
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-core:$kotlinxCoroutinesVersion")
    implementation ("org.jetbrains.kotlin:kotlin-reflect:$kotlinReflectVersion")
    implementation ("io.insert-koin:koin-core:$koinVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:$kotlinxSerializationJsonVersion")

    implementation(project(":isma-hsm"))
    implementation(project(":isma-lisma"))
    implementation(project(":isma-next-core"))
    implementation(project(":isma-intg-api"))
    implementation(project(":isma-next-integration-library"))
    implementation(project(":grin:integration"))
    implementation(project(":isma-next-common-services"))
    implementation(project(":isma-blueprint-editor"))
    implementation(project(":isma-text-editor"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-javafx:1.5.1-native-mt")
    implementation ("org.fxmisc.richtext:richtextfx:0.10.6")

    testImplementation(kotlin("test-junit"))
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "16"
}