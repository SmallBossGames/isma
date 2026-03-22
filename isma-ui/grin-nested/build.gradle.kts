plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.java.modules)
    alias(libs.plugins.javafx)
}

javafx {
    version = "25.0.2"
    modules = listOf("javafx.controls", "javafx.fxml")
}

dependencies {
    implementation(libs.tornadofx.core)
    implementation(libs.koin.core)

    api(project(":grin:gui:concatenation"))
    api(project(":grin:gui:common"))
}
