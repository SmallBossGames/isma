plugins {
    id("org.openjfx.javafxplugin")
    application
}

version = rootProject.version

javafx {
    version = "17.0.2"
    modules = listOf("javafx.controls", "javafx.fxml")
}

application {
    mainModule.set("isma.grin.app.main")
    mainClass.set("ru.nstu.isma.grin.launcher.LauncherKt")

    applicationDefaultJvmArgs = listOf(
        "--add-modules=jdk.incubator.vector",
        //"--add-opens=javafx.controls/javafx.scene.control=ALL-UNNAMED",
        //"--add-opens=javafx.graphics/javafx.scene=ALL-UNNAMED"
    )
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(libs.koin.core)

    implementation(project(":grin:integration"))

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}