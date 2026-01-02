plugins {
    alias(libs.plugins.kotlin.jvm)
    java
    alias(libs.plugins.java.modules)
    alias(libs.plugins.javafx)
}

javafx {
    version = "23.0.1"
    modules = listOf("javafx.controls")
}

dependencies {
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.javafx)

    testImplementation(libs.junit.jupiter.api)
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}
