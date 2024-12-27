plugins {
    alias(libs.plugins.kotlin.jvm)
    java
    alias(libs.plugins.java.modules)
     alias(libs.plugins.javafx)
}

val moduleName by extra("isma.isma.javafx.extensions.main")

javafx {
    version = "23.0.1"
    modules = listOf("javafx.controls")
}

dependencies {
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.javafx)

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}
