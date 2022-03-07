plugins {
    id("org.openjfx.javafxplugin")
}

javafx {
    version = "17"
    modules = listOf("javafx.controls", "javafx.graphics")
}

val koinVersion = "3.2.0-beta-1"

dependencies {
    implementation("no.tornado:tornadofx:1.7.20")
    implementation ("io.insert-koin:koin-core:$koinVersion")

    api(project(":grin:gui:concatenation"))
    api(project(":grin:gui:common"))
}
