plugins {
    alias(libs.plugins.kotlin.jvm)
    java
    alias(libs.plugins.java.modules)
    alias(libs.plugins.javafx)
}

javafx {
    version = "25.0.2"
    modules = listOf("javafx.controls")
}

dependencies {
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.javafx)

    testImplementation(libs.junit.jupiter.api)
    testRuntimeOnly(libs.junit.jupiter.engine)
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}
