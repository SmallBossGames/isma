plugins {
    kotlin("jvm")
    java

    id("org.openjfx.javafxplugin")
}

javafx {
    version = "17.0.2"
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