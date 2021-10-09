plugins {
    id("org.openjfx.javafxplugin")
}

javafx {
    version = "17"
    modules = listOf("javafx.controls", "javafx.graphics")
}

dependencies {
    implementation("no.tornado:tornadofx:1.7.20")
    api(project(":grin:gui:simple"))
    api(project(":grin:gui:concatenation"))
    api(project(":grin:gui:common"))
}
