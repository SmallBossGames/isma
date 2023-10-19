plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")

    id("org.javamodularity.moduleplugin")

    id("org.openjfx.javafxplugin")
}

val moduleName by extra("isma.isma.blueprint.editor.main")

javafx {
    version = "21.0.1"
    modules = listOf("javafx.controls", "javafx.fxml")
}

dependencies {
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.fxmisc.richtext.core)
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.javafx)
}
