plugins {
    alias(libs.plugins.kotlin.jvm)

    alias(libs.plugins.java.modules)

    alias(libs.plugins.javafx)
}

javafx {
    version = "25.0.2"
    modules = listOf("javafx.controls", "javafx.fxml", "javafx.swing")
}

dependencies {
    testImplementation("org.jetbrains.kotlin:kotlin-test:2.3.20")
    testImplementation("org.junit.jupiter:junit-jupiter:5.10.0")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.test {
    useJUnitPlatform()
    jvmArgs("-Djava.awt.headless=true")
}
