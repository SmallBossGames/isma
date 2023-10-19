plugins {
    kotlin("jvm")
    java
    id("org.javamodularity.moduleplugin")
    id("org.openjfx.javafxplugin")
}

val moduleName by extra("isma.isma.javafx.extensions.main")

javafx {
    version = "21.0.1"
    modules = listOf("javafx.controls")
}

dependencies {
    implementation(kotlin("stdlib"))

    implementation (libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.javafx)

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}
