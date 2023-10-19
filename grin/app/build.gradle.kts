plugins {
    id("org.openjfx.javafxplugin")
    id("org.javamodularity.moduleplugin")

    application
}

java {
    modularity.inferModulePath.set(false)
}

val moduleName by extra("isma.grin.app.main")

javafx {
    version = "21.0.1"
    modules = listOf("javafx.controls", "javafx.fxml")
}

application {
    mainModule.set("isma.grin.app.main")
    mainClass.set("ru.nstu.isma.grin.launcher.LauncherKt")
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