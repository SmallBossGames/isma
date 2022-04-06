plugins {
    kotlin("jvm")
    java

    id("org.openjfx.javafxplugin")
}

javafx {
    version = "17"
    modules = listOf("javafx.controls")
}

dependencies {
    implementation(kotlin("stdlib"))

    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-core:${PackageVersion.kotlinxCoroutines}")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-javafx:${PackageVersion.kotlinxCoroutines}")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}