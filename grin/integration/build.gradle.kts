plugins {
    id("org.openjfx.javafxplugin")
}

javafx {
    version = "17"
    modules = listOf("javafx.controls", "javafx.graphics")
}


dependencies {
    implementation("no.tornado:tornadofx:${PackageVersion.tornadoFx}")
    implementation("io.insert-koin:koin-core:${PackageVersion.koin}")

    api(project(":grin:gui:concatenation"))
    api(project(":grin:gui:common"))
}
