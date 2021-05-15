import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
    application
    id("org.openjfx.javafxplugin")
    id("com.github.johnrengelman.shadow")
}

javafx {
    version = "16"
    modules = listOf("javafx.controls", "javafx.fxml")
}

application {
    mainClass.set("launcher.LauncherKt")
    applicationDefaultJvmArgs = listOf(
        "--add-opens=javafx.controls/javafx.scene.control=ALL-UNNAMED",
        "--add-opens=javafx.graphics/javafx.scene=ALL-UNNAMED")
}

dependencies {
    implementation ("no.tornado:tornadofx:1.7.20")
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.4.1")
    implementation ("org.fxmisc.richtext:richtextfx:0.10.5")
    implementation ("org.antlr:antlr4-runtime:4.9")
    implementation ("org.jetbrains.kotlin:kotlin-reflect:1.4.30")

    implementation(project(":isma-hsm"))
    implementation(project(":isma-lisma"))
    implementation(project(":isma-next-core"))
    implementation(project(":isma-intg-api"))
    implementation(project(":isma-intg-lib:isma-intg-lib-common"))
    implementation(project(":grin:integration"))

    testImplementation(kotlin("test-junit"))
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "16"
}