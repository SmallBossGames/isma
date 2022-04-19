plugins {
    kotlin("jvm")
    id("org.openjfx.javafxplugin")
}

group = "ru.nstu.isma"
version = "1.0.0"

javafx {
    version = "17.0.2"
    modules = listOf("javafx.controls", "javafx.fxml")
}

dependencies {
    implementation(project(":isma-next-common-services"))
    implementation(project(":isma-lisma"))

    implementation(libs.fxmisc.richtext.core)
    implementation(libs.antlr4.runtime)
}
