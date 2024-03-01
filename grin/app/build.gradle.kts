plugins {
     alias(libs.plugins.javafx)
    alias(libs.plugins.java.modules)

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
    implementation(libs.koin.core)

    implementation(project(":grin:integration"))

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}