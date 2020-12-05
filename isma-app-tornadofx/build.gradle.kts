import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
    application
    id("org.openjfx.javafxplugin") version "0.0.9"
}

group = "me.smallboss"
version = "1.0-SNAPSHOT"



javafx {
    version = "11.0.2"
    modules("javafx.controls", "javafx.graphics")
}

application {
    mainClass.set("IsmaApp")
    applicationDefaultJvmArgs = listOf(
        "--add-opens=javafx.controls/javafx.scene.control=ALL-UNNAMED",
        "--add-opens=javafx.graphics/javafx.scene=ALL-UNNAMED")
}

dependencies {
    testImplementation(kotlin("test-junit"))
    implementation ("no.tornado:tornadofx:1.7.20")
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.4.1")
    implementation ("org.fxmisc.richtext:richtextfx:0.10.5")
    implementation ("org.antlr:antlr4-runtime:4.9")
    implementation(project(":isma-hsm"))
    implementation(project(":isma-lisma"))
    implementation(project(":isma-next-core"))
    implementation(project(":isma-intg-api"))
    implementation(project(":isma-intg-lib-parent"))
    implementation(project(":isma-intg-lib-parent:isma-intg-lib"))
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "11"
}