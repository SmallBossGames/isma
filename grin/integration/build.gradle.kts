plugins {
    id("org.openjfx.javafxplugin")
}

javafx {
    version = "17"
    modules = listOf("javafx.controls", "javafx.graphics")
}


dependencies {
    implementation(libs.tornadofx)
    implementation(libs.koin.core)

    api(project(":grin:gui:concatenation"))
    api(project(":grin:gui:common"))
}
