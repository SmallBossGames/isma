plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.java.modules)
}

group = "ru.nstu.isma"
version = "1.0.0"

dependencies {
    implementation(libs.com.google.guava)
    implementation ("org.apache.commons:commons-text:1.10.0")
    implementation ("org.slf4j:slf4j-api:2.0.5")

    implementation(project(":isma-intg-api"))
    implementation(project(":isma-next-tools"))
    implementation(project(":isma-compiler:hsm-core"))
}