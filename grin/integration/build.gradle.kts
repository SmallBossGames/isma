plugins {
     alias(libs.plugins.javafx)
    alias(libs.plugins.java.modules)
}

val moduleName by extra("isma.grin.integration.main")

javafx {
    version = "21.0.1"
    modules = listOf("javafx.controls", "javafx.graphics")
}

dependencies {
    implementation(libs.tornadofx.core)
    implementation(libs.koin.core)

    api(project(":grin:gui:concatenation"))
    api(project(":grin:gui:common"))
}
