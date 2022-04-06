plugins {
    id("org.openjfx.javafxplugin")
    application
}

version = rootProject.version

javafx {
    version = "17"
    modules = listOf("javafx.controls", "javafx.fxml")
}

application {
    mainClass.set("ru.nstu.isma.grin.launcher.LauncherKt")

    applicationDefaultJvmArgs = listOf(
        "--add-opens=javafx.controls/javafx.scene.control=ALL-UNNAMED",
        "--add-opens=javafx.graphics/javafx.scene=ALL-UNNAMED")
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("no.tornado:tornadofx:${PackageVersion.tornadoFx}")
    implementation("io.insert-koin:koin-core:${PackageVersion.koin}")

    implementation(project(":grin:integration"))

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}