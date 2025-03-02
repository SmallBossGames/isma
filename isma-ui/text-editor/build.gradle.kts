plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.java.modules)
    alias(libs.plugins.javafx)
}

javafx {
    version = "23.0.1"
    modules = listOf("javafx.controls", "javafx.fxml")
}

dependencies {
    implementation(project(":isma-compiler:lisma-translator-hsm"))

    implementation(libs.fxmisc.richtext.core)
    implementation(libs.antlr4.runtime)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.javafx)
}