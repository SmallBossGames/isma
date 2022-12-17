plugins {
    id("org.openjfx.javafxplugin")
    application
}

version = rootProject.version

javafx {
    version = "19"
    modules = listOf("javafx.controls", "javafx.fxml")
}

application {
    mainModule.set("isma.grin.app.main")
    mainClass.set("ru.nstu.isma.grin.launcher.LauncherKt")

    applicationDefaultJvmArgs = listOf(
        //"--add-opens=javafx.controls/javafx.scene.control=ALL-UNNAMED",
        //"--add-opens=javafx.graphics/javafx.scene=ALL-UNNAMED"
    )
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(libs.koin.core)

    implementation(project(":grin:integration"))

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}