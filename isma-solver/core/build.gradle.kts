plugins {
    alias(libs.plugins.java.modules)
    alias(libs.plugins.kotlin.jvm)
}

dependencies {
    implementation(project(":isma-solver:api"))
    implementation(project(":isma-compiler:hsm-jvm-calcmodel"))

    implementation("org.slf4j:slf4j-api:2.0.5")
    implementation("org.apache.commons:commons-lang3:3.12.0")
    implementation(libs.jetbrains.annotations)
}