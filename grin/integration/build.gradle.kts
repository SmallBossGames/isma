plugins {
    id("org.openjfx.javafxplugin")
}

javafx {
    version = "19"
    modules = listOf("javafx.controls", "javafx.graphics")
}


dependencies {
    implementation(libs.tornadofx.core)
    implementation(libs.koin.core)

    api(project(":grin:gui:concatenation"))
    api(project(":grin:gui:common"))
}
