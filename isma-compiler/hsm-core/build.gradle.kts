plugins {
    alias(libs.plugins.java.modules)
    alias(libs.plugins.kotlin.jvm)
}

dependencies {
    implementation(project(":isma-compiler:jvm-utils"))

    implementation("org.apache.commons:commons-lang3:3.12.0")
    implementation(libs.slf4j.api)
    implementation(libs.jetbrains.annotations)

    testImplementation(libs.junit)
}
