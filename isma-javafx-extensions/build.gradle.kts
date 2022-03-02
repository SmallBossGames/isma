plugins {
    kotlin("jvm")
    java

    id("org.openjfx.javafxplugin")
}

javafx {
    version = "17"
    modules = listOf("javafx.controls")
}

val kotlinxCoroutinesVersion = "1.6.0"

dependencies {
    implementation(kotlin("stdlib"))

    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-core:$kotlinxCoroutinesVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-javafx:$kotlinxCoroutinesVersion")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}