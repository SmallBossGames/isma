plugins {
    kotlin("jvm")

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

dependencies {
    implementation(kotlin("stdlib"))

    implementation(project(":isma-next-common-services"))
    implementation(project(":isma-lisma"))

    implementation ("no.tornado:tornadofx:1.7.20")
    implementation ("org.fxmisc.richtext:richtextfx:0.10.6")
    implementation ("org.antlr:antlr4-runtime:4.9")
}
