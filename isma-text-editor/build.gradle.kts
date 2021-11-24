plugins {
    kotlin("jvm")
    id("org.openjfx.javafxplugin")
}

group = "ru.nstu.isma"
version = "1.0.0"

javafx {
    version = "17"
    modules = listOf("javafx.controls", "javafx.fxml")
}

dependencies {
    implementation(project(":isma-next-common-services"))
    implementation(project(":isma-lisma"))

    implementation ("org.fxmisc.richtext:richtextfx:0.10.7")
    implementation ("org.antlr:antlr4-runtime:4.9.3")
}
