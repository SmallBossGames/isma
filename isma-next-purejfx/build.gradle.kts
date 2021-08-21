plugins {
    application
    kotlin("jvm")
    id("org.openjfx.javafxplugin")
    id("org.beryx.jlink") version "2.24.1"
}

group = "ru.nstu.isma"
version = "1.0-SNAPSHOT"

val junitVersion = "5.7.1"

application {
    mainModule.set("ru.nstu.isma.ismanextpurejfx")
    mainClass.set("ru.nstu.isma.ismanextpurejfx.HelloApplication")
}

javafx {
    version = "16"
    modules = listOf("javafx.controls", "javafx.fxml")
}

dependencies {
    implementation("org.controlsfx:controlsfx:11.1.0")
    implementation("com.dlsc.formsfx:formsfx-core:11.4.2")
    implementation("org.kordamp.ikonli:ikonli-javafx:12.2.0")
    implementation("org.kordamp.ikonli:ikonli-materialdesign2-pack:12.2.0")

    testImplementation("org.junit.jupiter:junit-jupiter-api:${junitVersion}")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:${junitVersion}")
}

jlink {
    launcher {
        name = "Launch"
    }
}