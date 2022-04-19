plugins {
    id("org.openjfx.javafxplugin")
}

javafx {
    version = "17.0.2"
    modules = listOf("javafx.controls", "javafx.graphics")
}


dependencies {
    implementation(libs.tornadofx.core)
    implementation(libs.koin.core)

    api(project(":grin:gui:concatenation"))
    api(project(":grin:gui:common"))
}
